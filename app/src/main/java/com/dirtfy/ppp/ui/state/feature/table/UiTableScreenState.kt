package com.dirtfy.ppp.ui.state.feature.table

import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder

data class UiTableScreenState(
    val tableList: List<UiTable> = emptyList(),
    val sourceTableList: List<UiTable> = emptyList(),
    val orderList: List<UiTableOrder> = emptyList(),
    val menuList: List<UiMenu> = emptyList(),

    val orderTotalPrice: String = "",
    val pointUse: UiPointUse = UiPointUse(),
    val timeLeftUntilEndOfMergeMode: String = "",
    val nowMenuCategory: MenuCategory = MenuCategory.ALCOHOL,
    val mode: UiTableMode = UiTableMode.Main,

    val tableListState: UiScreenState = UiScreenState(),
    val orderListState: UiScreenState = UiScreenState(),
    val menuListState: UiScreenState = UiScreenState(),
    val trySetMergeModeState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val escapeFromMergeModeState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val mergeTableState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableWithCashState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableWithCardState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val payTableWithPointState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val addOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val cancelOrderState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
