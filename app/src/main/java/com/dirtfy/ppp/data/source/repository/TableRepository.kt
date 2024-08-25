package com.dirtfy.ppp.data.source.repository

import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder

interface TableRepository {

    suspend fun readTables(): List<DataTable>
    suspend fun updateTable(table: DataTable)

    suspend fun mergeGroup(baseGroup: Int, mergingGroup: Int)

    suspend fun createOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    )
    suspend fun readOrders(group: Int): List<DataTableOrder>
    suspend fun updateOrder(tableNumber: Int, order: DataTableOrder)
    suspend fun deleteOrder(tableNumber: Int, menuName: String)
    suspend fun deleteAllOrder(tableNumber: Int)

    suspend fun getGroupNumber(tableNumber: Int): Int
    suspend fun getMemberTableNumbers(group: Int): List<Int>
    suspend fun getMenuCount(group: Int, menuName: String): Int

}