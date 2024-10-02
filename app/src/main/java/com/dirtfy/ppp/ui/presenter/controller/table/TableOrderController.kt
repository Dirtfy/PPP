package com.dirtfy.ppp.ui.presenter.controller.table

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableMode
import com.dirtfy.ppp.ui.dto.UiTableOrder
import kotlinx.coroutines.flow.StateFlow

interface TableOrderController {

    val orderList: StateFlow<FlowState<List<UiTableOrder>>>

    val orderTotalPrice: StateFlow<String>
    val pointUse: StateFlow<UiPointUse>

    val nowTable: StateFlow<Int>
    val mode: StateFlow<UiTableMode>

    fun updateOrderList(table: UiTable)
    fun updatePointUse(pointUse: UiPointUse)

    fun payTableWithCash()
    fun payTableWithCard()
    fun payTableWithPoint()
    fun addOrder(name: String, price: String)
    fun cancelOrder(name: String, price: String)

    fun setMode(mode: UiTableMode)
}