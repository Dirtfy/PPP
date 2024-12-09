package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder
import kotlinx.coroutines.flow.Flow

interface TableApi <TransactionType> {
    companion object {
        const val TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS = 30000L // 30초
    }

    fun createGroup(group: DataTableGroup, transaction: TransactionType): DataTableGroup
    fun deleteGroup(groupNumber: Int, orderList: List<DataTableOrder>, transaction: TransactionType)
    suspend fun readTable(tableNumber: Int): DataTable
    suspend fun readAllGroupedTable(): List<DataTable>
    fun checkTableGroupLock(transaction: TransactionType)
    fun getTableGroupLock(transaction: TransactionType)
    fun releaseTableGroupLock(transaction: TransactionType)
    fun tableStream(): Flow<List<DataTable>>

    suspend fun readOrder(groupNumber: Int, menuName: String): DataTableOrder
    fun readOrder(groupNumber: Int, menuName: String, transaction: TransactionType): DataTableOrder
    suspend fun readAllOrder(groupNumber: Int): List<DataTableOrder>
    suspend fun setOrder(groupNumber: Int, order: DataTableOrder)
    fun setOrder(groupNumber: Int, order: DataTableOrder, transaction: TransactionType)
    fun incrementOrder(groupNumber: Int, menuName: String, transaction: TransactionType)
    fun decrementOrder(groupNumber: Int, menuName: String, transaction: TransactionType)
    suspend fun deleteOrder(groupNumber: Int, menuName: String)
    fun deleteOrder(groupNumber: Int, menuName: String, transaction: TransactionType)
    suspend fun deleteAllOrder(groupNumber: Int)
    fun deleteOrders(groupNumber: Int, orderList: List<DataTableOrder>, transaction: TransactionType)
    fun orderStream(groupNumber: Int): Flow<List<DataTableOrder>>

    suspend fun isGroupExist(groupNumber: Int): Boolean
    fun isGroupExist(groupNumber: Int, transaction: TransactionType): Boolean
    suspend fun isOrderExist(groupNumber: Int, menuName: String): Boolean
    fun isOrderExist(groupNumber: Int, menuName: String, transaction: TransactionType): Boolean
}