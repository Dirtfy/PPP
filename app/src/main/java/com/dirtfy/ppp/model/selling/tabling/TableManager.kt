package com.dirtfy.ppp.model.selling.tabling

import android.util.Log
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.model.selling.TableModelContract.DTO.Table
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
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

        if (target == null || target == "") return false

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .get().await().documents.forEach {
                if (it.id == target) return true
            }

        return false
    }

    override suspend fun isSetup(tableNumberList: List<Int>): List<Boolean> {
        val physicalRefCapture = physicalRef.get().await()
        val logicalTableRefCapture = logicalRef
            .collection(RepositoryPath.TABLE_LOGICAL_TABLE).get().await()

        val resultList = mutableListOf<Boolean>()
        tableNumberList.forEach { tableNumber ->
            val target = physicalRefCapture.getString("$tableNumber")

            if (target == null || target == "") {
                resultList.add(false)
                return@forEach
            }

            val targetDocument = logicalTableRefCapture.documents.find { document ->
                document.id == target
            }

            resultList.add(targetDocument != null)
        }

        return resultList
    }

    override suspend fun setupTable(tableNumber: Int) {
        val logicalTable = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).document()

        logicalTable.set(_TableData()).await()

        physicalRef.update("$tableNumber", logicalTable.id).await()

        Log.d(TAG, "${logicalTable.id} - $tableNumber")
    }

    override suspend fun checkTable(tableNumber: Int): Table {
        val physicalRefCapture = physicalRef.get().await()
        val logicalTableID = physicalRefCapture.getString("$tableNumber")

        if (logicalTableID == null) {
            Log.d(TAG, "logical table lost!")
            throw IllegalAccessException()
        }

        Log.d(TAG, "$logicalTableID - $tableNumber")

        val _tableData = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTableID)
            .get().await().toObject(_TableData::class.java)?: throw IllegalAccessException()

        var actualTableNumber = 0
        (1..11).forEach {
            val logicalID = physicalRefCapture.getString("$it")

            if (logicalID == logicalTableID) {
                actualTableNumber = it
            }
        }

        return Table(
            tableNumber = actualTableNumber,
            menuNameList = _tableData.menuNameList?: listOf(),
            menuPriceList = _tableData.menuPriceList?: listOf()
        )
    }

    // TODO: 속도개선을 위해 필요할듯
    override suspend fun checkTables(tableNumberList: List<Int>): List<Table> {
        val physicalTableRef = physicalRef.get().await()
        val logicalTableRef = logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE).get().await()

        val documentMap = mutableMapOf<String, DocumentSnapshot>()
        logicalTableRef.documents.forEach {
            documentMap[it.id] = it
        }

        val actualNumberMap = mutableMapOf<String, Int>()
        (0..11).forEach {
            val key = physicalTableRef.getString("$it")!!
            actualNumberMap[key] = it
        }

        val tableDataList = mutableListOf<Table>()
        tableNumberList.forEach {
            val logicalID = physicalTableRef.getString("$it")
            val _tableData = documentMap[logicalID]!!.toObject(_TableData::class.java)!!

            tableDataList.add(
                Table(
                    tableNumber = actualNumberMap[logicalID]!!,
                    menuNameList = _tableData.menuNameList?: listOf(),
                    menuPriceList = _tableData.menuPriceList?: listOf()
                )
            )
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
        val physicalRefCapture = physicalRef.get().await()
        val logicalTargetTableID = physicalRefCapture.getString("$tableNumber")

        if (logicalTargetTableID == null) {
            Log.d(TAG, "logical table lost!")
            return
        }

        logicalRef.collection(RepositoryPath.TABLE_LOGICAL_TABLE)
            .document(logicalTargetTableID)
            .delete().await()

        (1..11).forEach {
            val logicalTableID = physicalRefCapture.getString("$it")

            if (logicalTableID == logicalTargetTableID) {
                setupTable(it)
            }
        }

    }

    override suspend fun mergeTable(baseTable: Table, mergingTable: Table) {
        val physicalRefCapture = physicalRef.get().await()
        val baseLogicalTableID = physicalRefCapture.getString("${baseTable.tableNumber}")
        val mergingLogicalTableID = physicalRefCapture.getString("${mergingTable.tableNumber}")

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

        (1..11).forEach {
            if (physicalRefCapture.getString("$it") == mergingLogicalTableID)
                physicalRef.update("$it", baseLogicalTableID).await()
        }
    }
    
}