package com.dirtfy.ppp.ui.dto.table.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.menu.UiMenu
import com.dirtfy.ppp.ui.dto.table.UiPointUse
import com.dirtfy.ppp.ui.dto.table.UiTable
import com.dirtfy.ppp.ui.dto.table.UiTableMode
import com.dirtfy.ppp.ui.dto.table.UiTableOrder

data class UiTableScreenState(
    val tableList: List<UiTable> = emptyList(),
    val sourceTableList: List<UiTable> = emptyList(),
    val orderList: List<UiTableOrder> = emptyList(),
    val menuList: List<UiMenu> = emptyList(),

    val orderTotalPrice: String = "",
    val pointUse: UiPointUse = UiPointUse(),

    val mode: UiTableMode = UiTableMode.Main,

    val tableListState: UiScreenState = UiScreenState(),
    val orderListState: UiScreenState = UiScreenState(),
    val menuListState: UiScreenState = UiScreenState(),
    val mergeTableState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val addOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val cancelOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
