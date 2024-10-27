package com.dirtfy.ppp.data.source.repository

import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import kotlinx.coroutines.flow.Flow

interface TableRepository {

    suspend fun readTable(tableNumber: Int): DataTable
    suspend fun readAllTable(): List<DataTable>
    suspend fun updateTable(table: DataTable)
    fun tableStream(): Flow<List<DataTable>>

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
    fun orderStream(tableNumber: Int): Flow<List<DataTableOrder>>

    suspend fun isOrderExist(tableNumber: Int, menuName: String): Boolean
}