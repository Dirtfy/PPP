package com.dirtfy.ppp.test.ui.dto.menu

import com.dirtfy.ppp.ui.dto.UiMenu

data class UiMenuState(
    val menuList: List<UiMenu> = emptyList(),
    val searchClue: String = "",
    val newMenu: UiMenu = UiMenu("", ""),
    val failMassage: String = "",
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
