package com.dirtfy.ppp.data.api.impl.feature.table.firebase

import android.icu.util.Calendar
import android.util.Log
import com.dirtfy.ppp.common.exception.ExternalException
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.TableApi.Companion.TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStorePath
import com.dirtfy.ppp.data.api.impl.common.firebase.Utils.convertToMilliseconds
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.FireStoreTableOrder.Companion.convertToFireStoreTableOrder
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder
import com.dirtfy.tagger.Tagger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class TableFireStore @Inject constructor(): TableApi, Tagger {

    private val tableRef = Firebase.firestore.collection(FireStorePath.TABLE)
    private val mergeLockRef = Firebase.firestore.document(FireStorePath.TABLE_GROUP_LOCK)
    private val groupIdRef = Firebase.firestore.document(FireStorePath.GROUP_ID_COUNT)

    private suspend fun getGroupNumber(tableNumber: Int): Int {
        val query = tableRef.whereArrayContains("member", tableNumber)
        val documents = query.get().await().documents

        return when (documents.size) {
            1 -> documents[0].id.toInt()
            0 -> DataTable.GROUP_NOT_ASSIGNED
            else -> throw TableException.NonUniqueGroup()
        }
    }
    private suspend fun readGroupMember(group: Int): List<Int> {
        val query = tableRef.document("$group")
        val result = query.get().await()
            .toObject(FireStoreGroup::class.java)!!

        return result.member?: emptyList()
    }

    override fun createGroup(group: DataTableGroup, transaction: Transaction): DataTableGroup {
        if (group.group != DataTable.GROUP_NOT_ASSIGNED)
            throw TableException.IllegalGroupIdAssignment()

        val snapshot = transaction.get(groupIdRef)
        val groupId = snapshot.getLong("count")!!.toInt()
        transaction.update(groupIdRef, "count", FieldValue.increment(1))

        val createdGroup = group.copy(group = groupId)
        transaction.set(tableRef.document("$groupId"), createdGroup)
        transaction.update(mergeLockRef, "last_occupied", Timestamp(Date(Calendar.getInstance().timeInMillis)))

        return createdGroup
    }

    override fun deleteGroup(groupNumber: Int, orderList: List<DataTableOrder>, transaction: Transaction) {
        val groupRef = tableRef.document("$groupNumber")
        val orderRef = getOrderRef(groupNumber)
        Log.d("WeGlonD", "delete group $groupNumber")
        orderList.forEach { order ->
            transaction.delete(orderRef.document(order.name))
        }
        transaction.delete(groupRef)
        Log.d("WeGlonD", "group$groupNumber deleted")
    }

    override suspend fun readTable(tableNumber: Int): DataTable {
        val group = getGroupNumber(tableNumber)

        return DataTable(
            number = tableNumber,
            group = group
        )
    }

    override suspend fun readAllGroupedTable(): List<DataTable> {
        val snapshot = tableRef.get().await()
        return readAllGroupedTable(snapshot)
    }

    private fun readAllGroupedTable( // group 있는 것들만 가져온다
        tableSnapshot: QuerySnapshot
    ): List<DataTable> {
        val resultList = mutableListOf<DataTable>()
        tableSnapshot.documents.forEach { document ->
            val group = document.toObject(FireStoreGroup::class.java)!!
            val memberList = group.member ?: throw TableException.MemberLoss()

            memberList.forEach { member ->
                resultList.add(
                    DataTable(
                        number = member,
                        group = document.id.toInt()
                    )
                )
            }
        }
        return resultList
    }

    override suspend fun isGroupExist(groupNumber: Int): Boolean {
        val groupRef = tableRef.document("$groupNumber")
        val document = groupRef.get().await()
        Log.d("donggi","in isGroupExist ${document.exists()}")
        return document.exists()
    }

    override fun isGroupExist(groupNumber: Int, transaction: Transaction): Boolean {
        val groupRef = tableRef.document("$groupNumber")
        val document = transaction.get(groupRef)
        Log.d("WeGlonD","in isGroupExist ${document.exists()}")
        return document.exists()
    }

    override fun checkTableGroupLock(transaction: Transaction) {
        val snapshot = transaction.get(mergeLockRef)
        val preoccupied = snapshot.getBoolean("occupied")!!
        val timestamp = snapshot.getTimestamp("last_occupied")!!
        if (preoccupied && !isExpiredLock(timestamp)) throw TableException.TableLockPreempted()
    }

    private fun isExpiredLock(lastOccupied: Timestamp): Boolean {
        val lastMillis = lastOccupied.convertToMilliseconds()
        val nowMillis = Calendar.getInstance().timeInMillis
        return nowMillis - lastMillis > TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS
    }

    override fun getTableGroupLock(transaction: Transaction) {
        transaction.update(mergeLockRef, "occupied", true)
        transaction.update(mergeLockRef, "last_occupied", Timestamp(Date(Calendar.getInstance().timeInMillis)))
    }

    override fun releaseTableGroupLock(transaction: Transaction) {
        transaction.update(mergeLockRef, "occupied", false)
    }

    override fun tableStream(): Flow<List<DataTable>> = callbackFlow {
        Log.d(TAG, "tableStream start")
        val tableSubscription = tableRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "tableStream snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "tableStream snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                /*
                if (snapshot.isEmpty) {
                    Log.e(TAG, "tableStream snapshot is empty")
                    throw ExternalException.ServerError()
                }*/

                val tableList = readAllGroupedTable(snapshot)
                trySend(tableList)
            } catch (e: Throwable) {
                Log.e(TAG, "table subscription fail\n${e.message}")
                close(e)
            }
        }
        Log.d(TAG, "tableStream end")
        awaitClose { tableSubscription.remove() }
    }

    private fun getOrderRef(tableNumber: Int): CollectionReference {
        return tableRef
            .document("$tableNumber")
            .collection(FireStorePath.TABLE_ORDER)
    }

    override suspend fun setOrder(groupNumber: Int, order: DataTableOrder) {
        Log.d("WeGlonD", "source setOrder")
        Log.d("WeGlonD", "$groupNumber")
        Log.d("WeGlonD", order.toString())

        val orderRef = getOrderRef(groupNumber)
        val firestoreOrder = order.convertToFireStoreTableOrder()
        orderRef.document(firestoreOrder.name!!).set(firestoreOrder).await()
    }

    override fun setOrder(groupNumber: Int, order: DataTableOrder, transaction: Transaction) {
        val orderRef = getOrderRef(groupNumber)
        val firestoreOrder = order.convertToFireStoreTableOrder()
        transaction.set(orderRef.document(firestoreOrder.name!!), firestoreOrder)
    }

    override suspend fun readOrder(groupNumber: Int, menuName: String): DataTableOrder {
        if(!isOrderExist(groupNumber,menuName)) throw TableException.InvalidOrderName()

        val order = getOrderRef(groupNumber)
            .document(menuName)
            .get().await()
            .toObject(FireStoreTableOrder::class.java)!!
            .convertToDataTableOrder()

        return order
    }

    override fun readOrder(
        groupNumber: Int,
        menuName: String,
        transaction: Transaction
    ): DataTableOrder {
        val orderRef = getOrderRef(groupNumber).document(menuName)
        val orderSnapshot = transaction.get(orderRef)
        if (!orderSnapshot.exists()) throw TableException.InvalidOrderName()
        return orderSnapshot.toObject(FireStoreTableOrder::class.java)!!.convertToDataTableOrder()
    }

    override suspend fun readAllOrder(groupNumber: Int): List<DataTableOrder> {
        val snapshot = getOrderRef(groupNumber).get().await()
        if(snapshot.isEmpty) return emptyList()
        return readAllOrder(snapshot)
    }

    private fun readAllOrder(orderSnapshot: QuerySnapshot): List<DataTableOrder> {
        return orderSnapshot.documents
            .map { it.toObject(FireStoreTableOrder::class.java)!! }
            .map { it.convertToDataTableOrder() }
    }

    override fun incrementOrder(groupNumber: Int, menuName: String, transaction: Transaction) {
        val orderRef = getOrderRef(groupNumber).document(menuName)
        transaction.update(orderRef, "count", FieldValue.increment(1))
    }

    override fun decrementOrder(groupNumber: Int, menuName: String, transaction: Transaction) {
        val orderRef = getOrderRef(groupNumber).document(menuName)
        transaction.update(orderRef, "count", FieldValue.increment(-1))
    }

    override suspend fun deleteOrder(groupNumber: Int, menuName: String) {
        if(!isOrderExist(groupNumber,menuName)) throw TableException.InvalidOrderName()

        getOrderRef(groupNumber)
            .document(menuName)
            .delete()
            .await()
    }

    override fun deleteOrder(groupNumber: Int, menuName: String, transaction: Transaction) {
        if(!isOrderExist(groupNumber,menuName,transaction)) throw TableException.InvalidOrderName()

        val orderRef = getOrderRef(groupNumber).document(menuName)
        transaction.delete(orderRef)
    }

    override suspend fun deleteAllOrder(groupNumber: Int) {
        val orderRef = getOrderRef(groupNumber)
        orderRef
            .get().await()
            .documents
            .forEach {
                orderRef.document(it.id).delete().await()
            }
    }

    override fun deleteOrders(
        groupNumber: Int,
        orderList: List<DataTableOrder>,
        transaction: Transaction
    ) {
        val orderRef = getOrderRef(groupNumber)
        orderList.forEach {
            transaction.delete(orderRef.document(it.name))
        }
    }

    override suspend fun isOrderExist(groupNumber: Int, menuName: String): Boolean {
        val orderRef = getOrderRef(groupNumber)
        val document = orderRef.document(menuName).get().await()
        Log.d("donggi","in isOrderExist${document.exists()}")
        return document.exists() // 이름 중복으로 firebase에 넣을 수 없다 무조건 0 or 1
    }

    override fun isOrderExist(
        groupNumber: Int,
        menuName: String,
        transaction: Transaction
    ): Boolean {
        val orderRef = getOrderRef(groupNumber)
        val document = transaction.get(orderRef.document(menuName))
        Log.d("donggi","in isOrderExist${document.exists()}")
        return document.exists()
    }

    override fun orderStream(groupNumber: Int): Flow<List<DataTableOrder>> = callbackFlow {
        val targetRef = getOrderRef(groupNumber)
        val orderSubscription = targetRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "orderStream snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "orderStream snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                val orderList = readAllOrder(snapshot)
                trySend(orderList)
            } catch (e: Throwable) {
                Log.e(TAG, "order subscription fail\n${e.message}")
                close(e)
            }
        }

        awaitClose { orderSubscription.remove() }
    }

}