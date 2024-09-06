package com.dirtfy.ppp.test.data.source.impl.firestore.table

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.table.FireStoreGroup
import com.dirtfy.ppp.data.source.firestore.table.FireStoreTableOrder
import com.dirtfy.ppp.data.source.firestore.table.FireStoreTableOrder.Companion.convertToFireStoreTableOrder
import com.dirtfy.ppp.data.source.repository.TableRepository
import com.dirtfy.ppp.test.data.source.TableSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TableFireStore @Inject constructor(
): TableSource {

    private val tableRef = Firebase.firestore.collection(FireStorePath.TABLE)

    private suspend fun readGroup(tableNumber: Int): Int {
        val query = tableRef.whereArrayContains("member", tableNumber)
        val documents = query.get().await().documents

        return when (documents.size) {
            1 -> documents[0].id.toInt()
            0 -> throw TableException.GroupLoss()
            else -> throw TableException.NonUniqueGroup()
        }
    }
    private suspend fun readGroupMember(group: Int): List<Int> {
        val query = tableRef.document("$group")
        val result = query.get().await()
            .toObject(FireStoreGroup::class.java)!!

        return result.member?: emptyList()
    }

    override suspend fun readTable(tableNumber: Int): DataTable {
        val group = readGroup(tableNumber)

        return DataTable(
            number = tableNumber,
            group = group
        )
    }

    private fun readAllTable(
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
    override suspend fun readAllTable(): List<DataTable> {
        val tableSnapshot = tableRef.get().await()

        return readAllTable(tableSnapshot)
    }

    override suspend fun updateTable(table: DataTable) {
        val nowGroup = readGroup(table.number)

        val nowGroupMember = readGroupMember(nowGroup)
        tableRef.document("$nowGroup").set(
            FireStoreGroup(member = nowGroupMember.filter { it != table.number })
        ).await()

        val targetGroupMember = readGroupMember(table.group)
        tableRef.document("${table.group}").set(
            FireStoreGroup(member = targetGroupMember + table.number)
        ).await()
    }

    private fun getOrderRef(tableNumber: Int): CollectionReference {
        return tableRef
            .document("$tableNumber")
            .collection(FireStorePath.TABLE_ORDER)
    }

    private suspend fun getOrderID(tableNumber: Int, menuName: String): String? {
        val orderRef = getOrderRef(tableNumber)
        val query = orderRef.whereEqualTo("name", menuName)
        val documents = query.get().await().documents

        return when (documents.size) {
            1 -> documents[0].id
            0 -> null
            else -> throw TableException.NonUniqueOrderName()
        }
    }
    private suspend fun setOrder(tableNumber: Int, order: FireStoreTableOrder) {
        val orderID = getOrderID(tableNumber, order.name!!)

        val orderRef = tableRef
            .document("$tableNumber")
            .collection(FireStorePath.TABLE_ORDER)

        if (orderID == null)
            orderRef.document().set(order).await()
        else
            orderRef.document(orderID).set(order).await()
    }

    override suspend fun createOrder(tableNumber: Int, menuName: String, menuPrice: Int) {
        val orderRef = getOrderRef(tableNumber)

        orderRef.document().set(
            FireStoreTableOrder(
                name = menuName,
                price = menuPrice,
                count = 1
            )
        ).await()
    }

    override suspend fun readOrder(tableNumber: Int, menuName: String): DataTableOrder {
        val orderID = getOrderID(tableNumber, menuName)
            ?: throw TableException.InvalidOrderName()

        val order = getOrderRef(tableNumber)
            .document(orderID)
            .get().await()
            .toObject(FireStoreTableOrder::class.java)!!
            .convertToDataTableOrder()

        return DataTableOrder(
            name = menuName,
            price = order.price,
            count = order.count
        )
    }

    private fun readAllOrder(
        orderSnapshot: QuerySnapshot
    ): List<DataTableOrder> {
        return orderSnapshot
            .documents
            .map { it.toObject(FireStoreTableOrder::class.java)!! }
            .map { it.convertToDataTableOrder() }
    }
    override suspend fun readAllOrder(tableNumber: Int): List<DataTableOrder> {
        val orderSnapshot = getOrderRef(tableNumber).get().await()

        return readAllOrder(orderSnapshot)
    }

    override suspend fun updateOrder(tableNumber: Int, order: DataTableOrder) {
        getOrderID(tableNumber, order.name)
            ?: throw TableException.InvalidOrderName()

        setOrder(tableNumber, order.convertToFireStoreTableOrder())
    }

    override suspend fun deleteOrder(tableNumber: Int, menuName: String) {
        val orderID = getOrderID(tableNumber, menuName)
            ?: throw TableException.InvalidOrderName()

        getOrderRef(tableNumber)
            .document(orderID)
            .delete()
            .await()
    }

    override suspend fun deleteAllOrder(tableNumber: Int) {
        val orderRef = getOrderRef(tableNumber)
        orderRef
            .get().await()
            .documents
            .forEach {
                orderRef.document(it.id).delete().await()
            }
    }

    override fun tableStream(): Flow<List<DataTable>>
    = callbackFlow {
        val subscription = tableRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends events to the flow! Consumers will get the new events
            try {
                val tableList = readAllTable(snapshot)

                trySend(tableList)
            } catch (error: Throwable) {
                // Event couldn't be sent to the flow
            }
        }

        awaitClose { subscription.remove() }
    }

    override fun tableOrderStream(tableNumber: Int): Flow<List<DataTableOrder>>
    = callbackFlow {
        val subscription = getOrderRef(tableNumber).addSnapshotListener { snapshot, error ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends events to the flow! Consumers will get the new events
            try {
                val orderList = readAllOrder(snapshot)

                trySend(orderList)
            } catch (error: Throwable) {
                // Event couldn't be sent to the flow
            }
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun isOrderExist(tableNumber: Int, menuName: String): Boolean {
        return getOrderID(tableNumber, menuName) != null
    }

}