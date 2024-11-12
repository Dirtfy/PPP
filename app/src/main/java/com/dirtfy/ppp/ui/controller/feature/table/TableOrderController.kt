package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.state.feature.table.UiTableOrderScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import kotlinx.coroutines.flow.Flow

interface TableOrderController {
    val screenData: Flow<UiTableOrderScreenState>

    suspend fun updateOrderList(table: UiTable)
    fun updatePointUse(pointUse: UiPointUse)
    suspend fun payTableWithCash(tableNumber: Int)
    suspend fun payTableWithCard(tableNumber: Int)
    suspend fun payTableWithPoint(tableNumber: Int)
    suspend fun addOrder(tableNumber: Int, name: String, price: String)
    suspend fun cancelOrder(tableNumber: Int, name: String, price: String)
}