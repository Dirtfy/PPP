package com.dirtfy.ppp.selling.tabling.model

import android.util.Log
import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object TableManager {

    private const val TAG = "TableManager"
    
    private val repositoryRef = 
        Firebase.firestore.collection(RepositoryPath.TABLE)
    private val physicalRef = 
        repositoryRef.document(RepositoryPath.TABLE_PHYSICAL)
    private val logicalRef = 
        repositoryRef.document(RepositoryPath.TABLE_LOGICAL)

    suspend fun setupTable(tableNumber: Int) {
        val target = physicalRef.get().await().getString("$tableNumber")
        if (target != null && target != "")
            return

        val logicalTable = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).document()

        logicalTable.set(_TableData()).await()

        physicalRef.update("$tableNumber", logicalTable.id).await()
    }

    suspend fun checkTable(tableNumber: Int): TableData {
        val logicalTableID = physicalRef.get().await().getString("$tableNumber")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            throw IllegalAccessException()
        }

        Log.d(TAG, logicalTableID)

        val _tableData = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .get().await().toObject(_TableData::class.java)?: throw IllegalAccessException()

        return TableData(
            tableNumber = tableNumber,
            menuNameList = _tableData.menuNameList?: listOf(),
            menuPriceList = _tableData.menuPriceList?: listOf()
        )
    }

    suspend fun checkTables(tableNumberList: List<Int>): List<TableData> {
        val physicalTableRef = physicalRef.get().await()
        val logicalTableRef = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).get().await()

        val logicalTableIDs = mutableListOf<String>()
        tableNumberList.forEach {
            logicalTableIDs.add(
                physicalTableRef.getString("$it")?:
                throw IllegalAccessException()
            )
        }

        val tableDataList = mutableListOf<TableData>()
        logicalTableIDs.indices.forEach { index ->
            logicalTableRef.documents.forEach { snapshot ->
                if (snapshot.id == logicalTableIDs[index]) {
                    val menuNameList = snapshot.getField<List<String>>("menuNameList")?: throw IllegalAccessException()
                    val menuPriceList = snapshot.getField<List<Int>>("menuPriceList")?: throw IllegalAccessException()
                    tableDataList.add(
                        TableData(
                            tableNumberList[index],
                            menuNameList,
                            menuPriceList
                        )
                    )
                }
            }
        }

        return tableDataList
    }

    suspend fun updateMenu(tableData: TableData) {
        val logicalTableID = physicalRef.get().await().getString("${tableData.tableNumber}")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .set(_TableData(
                ArrayList(tableData.menuNameList),
                ArrayList(tableData.menuPriceList)
            )).await()
    }
    
    suspend fun cleanTable(tableNumber: Int) {
        val logicalTableID = physicalRef.get().await().getString("$tableNumber")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .delete().await()
    }

    suspend fun mergeTable(baseTable: TableData, mergingTable: TableData) {
        val baseLogicalTableID = physicalRef.get().await().getString("${baseTable.tableNumber}")
        val mergingLogicalTableID = physicalRef.get().await().getString("${mergingTable.tableNumber}")

        if (baseLogicalTableID == null || mergingLogicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(mergingLogicalTableID)
            .delete().await()

        val _mergedTableData = _TableData(
            ArrayList(baseTable.menuNameList + mergingTable.menuNameList),
            ArrayList(baseTable.menuPriceList + mergingTable.menuPriceList)
        )

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(baseLogicalTableID)
            .set(_mergedTableData).await()

        physicalRef.update("${mergingTable.tableNumber}", baseLogicalTableID).await()

    }
    
}