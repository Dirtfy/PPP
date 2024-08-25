package com.dirtfy.ppp.data.source.firestore.table

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.table.FireStoreTable.Companion.convertToFireStoreTable
import com.dirtfy.ppp.data.source.repository.TableRepository
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class TableFireStore: TableRepository {

    private val tableRef = Firebase.firestore.collection(FireStorePath.TABLE)

    override suspend fun readTables(): List<DataTable> {
        val documents = tableRef
            .document(FireStorePath.TABLE_MAPPING)
            .collection(FireStorePath.TABLE_NUMBER)
            .get().await().documents

        return documents.map {
            it.toObject(FireStoreTable::class.java)!!
        }.map {
            it.convertToDataTable()
        }
    }

    override suspend fun updateTable(table: DataTable) {
        // number -> group update
        val mappingRef = tableRef.document(FireStorePath.TABLE_MAPPING)

        val groupRef = mappingRef
            .collection(FireStorePath.TABLE_NUMBER)

        val groupQuery = groupRef
            .whereEqualTo("number", table.number)

        val documents = groupQuery.get().await().documents

        val documentID = when(documents.size) {
            0 -> throw TableException.GroupLoss()
            1 -> documents[0].id
            else -> throw TableException.NonUniqueGroup()
        }

        groupRef.document(documentID).set(
            table.convertToFireStoreTable()
        )

        // group -> number delete
        val originalGroupRef = mappingRef
            .collection(FireStorePath.TABLE_GROUP)

        val originalGroupQuery = originalGroupRef
            .whereArrayContains("memberList", table.number)

        val originalGroupDocuments = originalGroupQuery.get().await().documents

        val originalGroupDocumentID: String
        val originalGroup = when(originalGroupDocuments.size) {
            0 -> throw TableException.GroupLoss()
            1 -> {
                originalGroupDocumentID = documents[0].id
                documents[0].toObject(FireStoreGroup::class.java)!!
            }
            else -> throw TableException.NonUniqueGroup()
        }

        originalGroupRef.document(originalGroupDocumentID)
            .set(originalGroup.copy(
                memberList = originalGroup.memberList?.filter { it == table.number }
            ))

        // group -> number add
        val newGroupRef = mappingRef
            .collection(FireStorePath.TABLE_GROUP)

        val newGroupQuery = newGroupRef
            .whereEqualTo("group", table.group)

        val newGroupDocuments = newGroupQuery.get().await().documents

        val newGroupDocumentID: String
        val newGroup = when(newGroupDocuments.size) {
            0 -> throw TableException.GroupLoss()
            1 -> {
                newGroupDocumentID = documents[0].id
                documents[0].toObject(FireStoreGroup::class.java)!!
            }
            else -> throw TableException.NonUniqueGroup()
        }

        newGroupRef.document(newGroupDocumentID)
            .set(newGroup.copy(
                memberList = newGroup.memberList?.plus(table.number)
            ))
    }

    override suspend fun mergeGroup(baseGroup: Int, mergingGroup: Int) {
        // TODO
        val baseGroupRef = tableRef
            .document(FireStorePath.TABLE_MAPPING)
            .collection(FireStorePath.TABLE_GROUP)
            .document("$baseGroup")

        val mergingGroupRef = tableRef
            .document(FireStorePath.TABLE_MAPPING)
            .collection(FireStorePath.TABLE_GROUP)
            .document("$mergingGroup")

        val mergingMemberList = mergingGroupRef.get().await()
            .get(FireStorePath.TABLE_GROUP_FIELD) as List<*>

        mergingGroupRef.update(FireStorePath.TABLE_GROUP_FIELD, emptyList<Int>())

        val baseMemberList = baseGroupRef.get().await()
            .get(FireStorePath.TABLE_GROUP_FIELD) as List<*>

        baseGroupRef.update(
            FireStorePath.TABLE_GROUP_FIELD,
            baseMemberList + mergingMemberList
        )
    }

    override suspend fun createOrder(tableNumber: Int, menuName: String, menuPrice: Int) {
        // TODO
    }

    override suspend fun readOrders(group: Int): List<DataTableOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(tableNumber: Int, order: DataTableOrder) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(tableNumber: Int, menuName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllOrder(tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupNumber(tableNumber: Int): Int {
        // TODO
        var found = false
        var group = 0

        val document = tableRef.document(FireStorePath.TABLE_MAPPING).get().await()
        (1..11).forEach { groupNumber ->
            val groupList = document.get("$groupNumber") as? List<*>
                ?: throw TableException.GroupLoss()

            if (groupList.map { it as Int }.contains(tableNumber)) {
                if (found) throw TableException.InValidGroupState()
                group = groupNumber
                found = true
            }
        }

        if (!found) throw TableException.InValidGroupState()

        return group
    }

    override suspend fun getMemberTableNumbers(group: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getMenuCount(group: Int, menuName: String): Int {
        TODO("Not yet implemented")
    }

}