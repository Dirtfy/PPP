package com.dirtfy.ppp.test.data.logic

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import kotlinx.coroutines.flow.Flow

interface TableLogic {


    fun readTables(): Flow<List<DataTable>>

    fun readOrders(tableNumber: Int): Flow<List<DataTableOrder>>

    fun mergeTables(tableNumberList: List<Int>): Flow<Unit>

    fun payTableWithCash(
        tableNumber: Int
    ): Flow<Unit>
    fun payTableWithCard(
        tableNumber: Int
    ): Flow<Unit>
    fun payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ): Flow<Unit>

    fun addOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ): Flow<DataTableOrder>
    fun cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ): Flow<DataTableOrder>

    fun getGroup(tableNumber: Int): Flow<Int>

    fun tableStream(): Flow<List<DataTable>>
    fun tableOrderStream(tableNumber: Int): Flow<List<DataTableOrder>>

}