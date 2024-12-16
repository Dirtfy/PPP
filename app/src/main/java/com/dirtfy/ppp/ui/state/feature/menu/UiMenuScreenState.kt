package com.dirtfy.ppp.ui.state.feature.menu

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiNewMenu

data class UiMenuScreenState(
    val menuList: List<UiMenu> = emptyList(),
    val searchClue: String = "",
    val newMenu: UiNewMenu = UiNewMenu(),

    val menuListState: UiScreenState = UiScreenState(),
    val createMenuState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val deleteMenuState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
