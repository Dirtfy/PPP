package com.dirtfy.ppp.ui.dto.menu.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.menu.UiMenu

data class UiMenuScreenState(
    val menuList: List<UiMenu> = emptyList(),
    val searchClue: String = "",
    val newMenu: UiMenu = UiMenu(),

    val menuListState: UiScreenState = UiScreenState(),
    val newMenuState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val deleteMenuState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
