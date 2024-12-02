package com.dirtfy.ppp.ui.state.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder

data class UiTableOrderScreenState(
    val orderList: List<UiTableOrder> = emptyList(),
    val orderTotalPrice: String = "",
    val pointUse: UiPointUse = UiPointUse(),

    val orderListState: UiScreenState = UiScreenState(),
    val payTableWithCashState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableWithCardState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableWithPointState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val addOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val cancelOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
