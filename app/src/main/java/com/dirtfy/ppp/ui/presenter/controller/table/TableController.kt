package com.dirtfy.ppp.ui.presenter.controller.table

import com.dirtfy.ppp.ui.dto.table.UiPointUse
import com.dirtfy.ppp.ui.dto.table.UiTable
import com.dirtfy.ppp.ui.dto.table.UiTableMode
import com.dirtfy.ppp.ui.dto.table.screen.UiTableScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface TableController: Controller {

    val screenData: StateFlow<UiTableScreenState>

    suspend fun updateTableList()
    suspend fun updateOrderList(table: UiTable)
    suspend fun updateMenuList()
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

    fun request(job: suspend TableController.() -> Unit)
}