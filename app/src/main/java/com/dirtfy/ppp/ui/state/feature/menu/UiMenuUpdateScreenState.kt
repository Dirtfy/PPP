package com.dirtfy.ppp.ui.state.feature.menu

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu

data class UiMenuUpdateScreenState(
    val newMenu: UiMenu = UiMenu(),

    val addMenuState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val deleteMenuState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
