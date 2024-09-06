package com.dirtfy.ppp.test.data.source

import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import kotlinx.coroutines.flow.Flow

interface TableSource {

    suspend fun readTable(tableNumber: Int): DataTable
    suspend fun readAllTable(): List<DataTable>
    suspend fun updateTable(table: DataTable)

    suspend fun createOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    )
    suspend fun readOrder(tableNumber: Int, menuName: String): DataTableOrder
    suspend fun readAllOrder(tableNumber: Int): List<DataTableOrder>
    suspend fun updateOrder(tableNumber: Int, order: DataTableOrder)
    suspend fun deleteOrder(tableNumber: Int, menuName: String)
    suspend fun deleteAllOrder(tableNumber: Int)

    fun tableStream(): Flow<List<DataTable>>
    fun tableOrderStream(tableNumber: Int): Flow<List<DataTableOrder>>

    suspend fun isOrderExist(tableNumber: Int, menuName: String): Boolean
}