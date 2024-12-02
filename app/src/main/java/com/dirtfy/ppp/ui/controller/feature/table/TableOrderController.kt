package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableOrderScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import kotlinx.coroutines.flow.Flow

interface TableOrderController {
    val screenData: Flow<UiTableOrderScreenState>

    fun updateOrderList(groupNumber: Int)
    fun retryUpdateOrderList()
    fun updatePointUse(pointUse: UiPointUse)
    suspend fun payTableWithCash(groupNumber: Int)
    suspend fun payTableWithCard(groupNumber: Int)
    suspend fun payTableWithPoint(groupNumber: Int)
    suspend fun addOrder(selectedTable: DataTable, name: String, price: String)
    suspend fun cancelOrder(selectedTable: DataTable, name: String, price: String)
    fun setPayTableWithCashState(state: UiScreenState)
    fun setPayTableWithCardState(state: UiScreenState)
    fun setPayTableWithPointState(state: UiScreenState)
    fun setOrderListState(state: UiScreenState)
    fun setAddOrderState(state: UiScreenState)
    fun setCancelOrderState(state: UiScreenState)
}