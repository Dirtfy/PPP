package com.dirtfy.ppp.model.selling.tabling

import android.util.Log
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.model.selling.TableModelContract.DTO.Table
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object TableManager: TableModelContract.API, Tagger {
    
    private val repositoryRef = 
        Firebase.firestore.collection(RepositoryPath.TABLE)
    private val physicalRef = 
        repositoryRef.document(RepositoryPath.TABLE_PHYSICAL)
    private val logicalRef = 
        repositoryRef.document(RepositoryPath.TABLE_LOGICAL)

    override suspend fun isSetup(tableNumber: Int): Boolean {
        val target = physicalRef.get().await().getString("$tableNumber")

        return target != null && target != ""
    }

    override suspend fun setupTable(tableNumber: Int) {
        val logicalTable = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).document()

        logicalTable.set(_TableData()).await()

        physicalRef.update("$tableNumber", logicalTable.id).await()
    }

    override suspend fun checkTable(tableNumber: Int): Table {
        val logicalTableID = physicalRef.get().await().getString("$tableNumber")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            throw IllegalAccessException()
        }

        Log.d(TAG, logicalTableID)

        val _tableData = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .get().await().toObject(_TableData::class.java)?: throw IllegalAccessException()

        return Table(
            tableNumber = tableNumber,
            menuNameList = _tableData.menuNameList?: listOf(),
            menuPriceList = _tableData.menuPriceList?: listOf()
        )
    }

    suspend fun checkTables(tableNumberList: List<Int>): List<Table> {
        val physicalTableRef = physicalRef.get().await()
        val logicalTableRef = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).get().await()

        val logicalTableIDs = mutableListOf<String>()
        tableNumberList.forEach {
            logicalTableIDs.add(
                physicalTableRef.getString("$it")?:
                throw IllegalAccessException()
            )
        }

        val tableDataList = mutableListOf<Table>()
        logicalTableIDs.indices.forEach { index ->
            logicalTableRef.documents.forEach { snapshot ->
                if (snapshot.id == logicalTableIDs[index]) {
                    val menuNameList = snapshot.getField<List<String>>("menuNameList")?: throw IllegalAccessException()
                    val menuPriceList = snapshot.getField<List<Int>>("menuPriceList")?: throw IllegalAccessException()
                    tableDataList.add(
                        Table(
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

    override suspend fun updateMenu(tableData: Table) {
        val logicalTableID = physicalRef.get().await().getString("${tableData.tableNumber}")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .set(
                _TableData(
                ArrayList(tableData.menuNameList),
                ArrayList(tableData.menuPriceList)
            )
            ).await()
    }
    
    override suspend fun cleanTable(tableNumber: Int) {
        val logicalTargetTableID = physicalRef.get().await().getString("$tableNumber")

        if (logicalTargetTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTargetTableID)
            .delete().await()

        (0..10).forEach {
            val logicalTableID = physicalRef.get().await().getString("$it")

            if (logicalTableID == logicalTargetTableID) {
                physicalRef.update("$it", "").await()
            }
        }

    }

    override suspend fun mergeTable(baseTable: Table, mergingTable: Table) {
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