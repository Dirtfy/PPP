package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.Flow

interface TableApi {

    fun createGroup(group: DataTableGroup, transaction: Transaction): DataTableGroup
    fun deleteGroup(groupNumber: Int, transaction: Transaction)
    suspend fun readTable(tableNumber: Int): DataTable
    suspend fun readAllTable(): List<DataTable>
    suspend fun updateTable(table: DataTable)
    fun combineGroup(group1: DataTableGroup, group2: DataTableGroup, transaction: Transaction): DataTableGroup
    fun tableStream(): Flow<List<DataTable>>

    /*suspend fun createOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    )*/
    suspend fun readOrder(groupNumber: Int, menuName: String): DataTableOrder
    fun readOrder(groupNumber: Int, menuName: String, transaction: Transaction): DataTableOrder
    suspend fun readAllOrder(groupNumber: Int): List<DataTableOrder>
    /*suspend fun updateOrder(tableNumber: Int, order: DataTableOrder)*/
    suspend fun setOrder(groupNumber: Int, order: DataTableOrder)
    fun setOrder(groupNumber: Int, order: DataTableOrder, transaction: Transaction)
    suspend fun deleteOrder(groupNumber: Int, menuName: String)
    fun deleteOrder(groupNumber: Int, menuName: String, transaction: Transaction)
    suspend fun deleteAllOrder(groupNumber: Int)
    fun deleteOrders(groupNumber: Int, orderList: List<DataTableOrder>, transaction: Transaction)
    fun orderStream(groupNumber: Int): Flow<List<DataTableOrder>>

    suspend fun isGroupExist(groupNumber: Int): Boolean
    fun isGroupExist(groupNumber: Int, transaction: Transaction): Boolean
    suspend fun isOrderExist(groupNumber: Int, menuName: String): Boolean
    fun isOrderExist(groupNumber: Int, menuName: String, transaction: Transaction): Boolean
}