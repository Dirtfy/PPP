package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode

interface TableController: Controller<UiTableScreenState, TableController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateTableList()
    fun retryUpdateTableList()
    fun updateOrderList(table: UiTable)
    fun retryUpdateOrderList()
    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()
    fun retryUpdateMenuList()
    fun updatePointUse(pointUse: UiPointUse)

    fun clickTable(table: UiTable)
    suspend fun mergeTable()
    fun cancelMergeTable()
    suspend fun payTableWithCash()
    suspend fun payTableWithCard()
    suspend fun payTableWithPoint()
    suspend fun addOrder(name: String, price: String)
    suspend fun cancelOrder(name: String, price: String)

    fun setMode(mode: UiTableMode)
    fun setMenuListState(state: UiScreenState)
    fun setTableListState(state: UiScreenState)
    fun setMergeTableState(state: UiScreenState)
    fun setPayTableWithCashState(state: UiScreenState)
    fun setPayTableWithCardState(state: UiScreenState)
    fun setPayTableWithPointState(state: UiScreenState)
    fun setOrderListState(state: UiScreenState)
    fun setAddOrderState(state: UiScreenState)
    fun setCancelOrderState(state: UiScreenState)
}