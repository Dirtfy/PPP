package com.dirtfy.ppp.model.selling.recording

import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract
import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract.DTO.Sales
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object SalesRecordRepository: SalesRecordModelContract.API {

    private const val TAG = "SalesRecordManager"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.SALES_RECORD)

    private fun Sales.convertToInternalData(): _SalesData {
        val menuNameList = menuCountMap.keys.toList()
        val menuPriceList = menuNameList.map { menuPriceMap[it]!! }

        return _SalesData(
            menuNameList = ArrayList(menuNameList),
            menuPriceList = ArrayList(menuPriceList),
            pointAccountNumber = pointAccountNumber
        )
    }

    private fun _SalesData.convertToExternalData(): Sales {
        val countMap = HashMap<String, Int>()
        val priceMap = HashMap<String, Int>()

        menuNameList?.indices?.forEach {
            if (countMap[menuNameList[it]] == null) {
                countMap[menuNameList[it]] = 1
            }
            else {
                countMap[menuNameList[it]] = countMap[menuNameList[it]]!! + 1
            }

            priceMap[menuNameList[it]] = menuPriceList?.get(it)?: throw IllegalAccessException()
        }

        return Sales(
            salesID = null,
            menuCountMap = countMap,
            menuPriceMap = priceMap,
            pointAccountNumber = pointAccountNumber
        )
    }

    override suspend fun create(data: Sales): Sales {
        val newSalesRef = repositoryRef.document()

        newSalesRef.set(data.convertToInternalData())

        return data.copy(salesID = newSalesRef.id)
    }

    override suspend fun read(filter: (Sales) -> Boolean): List<Sales> {
        val accountList = mutableListOf<Sales>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _salesData = documentSnapshot.toObject<_SalesData>()!!
            val salesData = _salesData
                .convertToExternalData().copy(salesID = documentSnapshot.id)

            if (!filter(salesData)) return@forEach

            accountList.add(salesData)
        }

        return accountList
    }

    override suspend fun update(filter: (Sales) -> Sales) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(filter: (Sales) -> Boolean) {
        TODO("Not yet implemented")
    }
}