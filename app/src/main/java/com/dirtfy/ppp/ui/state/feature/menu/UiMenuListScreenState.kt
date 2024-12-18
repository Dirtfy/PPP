package com.dirtfy.ppp.ui.state.feature.menu

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu

data class UiMenuListScreenState(
    val menuList: List<UiMenu> = emptyList(),
    val searchClue: String = "",
    val searchAlcohol: Boolean = false,
    val searchLunch: Boolean = false,
    val searchDinner: Boolean = false,

    val menuListState: UiScreenState = UiScreenState(),
)
