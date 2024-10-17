package com.dirtfy.ppp.ui.presenter.controller.table

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.menu.UiMenu
import com.dirtfy.ppp.ui.dto.table.UiPointUse
import com.dirtfy.ppp.ui.dto.table.UiTable
import com.dirtfy.ppp.ui.dto.table.UiTableMode
import com.dirtfy.ppp.ui.dto.table.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface TableController: Controller {

    val tableList: StateFlow<FlowState<List<UiTable>>>
    val orderList: StateFlow<FlowState<List<UiTableOrder>>>
    val menuList: StateFlow<FlowState<List<UiMenu>>>

    val orderTotalPrice: StateFlow<String>
    val pointUse: StateFlow<UiPointUse>

    val mode: StateFlow<UiTableMode>

    fun updateTableList()
    fun updateOrderList(table: UiTable)
    fun updateMenuList()
    fun updatePointUse(pointUse: UiPointUse)

    fun clickTable(table: UiTable)
    fun mergeTable()
    fun cancelMergeTable()
    fun payTableWithCash()
    fun payTableWithCard()
    fun payTableWithPoint()
    fun addOrder(name: String, price: String)
    fun cancelOrder(name: String, price: String)

    fun setMode(mode: UiTableMode)
}