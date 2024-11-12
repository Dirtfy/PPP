package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMergeScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode

interface TableController: Controller<UiTableScreenState, TableController> {

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
    fun setMergeMode(mode: UiScreenState)

}