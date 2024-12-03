package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableOrderScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import kotlinx.coroutines.flow.Flow

interface TableOrderController {
    val screenData: Flow<UiTableOrderScreenState>

    fun updateOrderList(groupNumber: Int)
    fun retryUpdateOrderList()
    fun updatePointUse(pointUse: UiPointUse)
    suspend fun payTableWithCash()
    suspend fun payTableWithCard()
    suspend fun payTableWithPoint()
    suspend fun addOrder(name: String, price: String)
    suspend fun cancelOrder(name: String, price: String)
    fun setPayTableWithCashState(state: UiScreenState)
    fun setPayTableWithCardState(state: UiScreenState)
    fun setPayTableWithPointState(state: UiScreenState)
    fun setOrderListState(state: UiScreenState)
    fun setAddOrderState(state: UiScreenState)
    fun setCancelOrderState(state: UiScreenState)
}