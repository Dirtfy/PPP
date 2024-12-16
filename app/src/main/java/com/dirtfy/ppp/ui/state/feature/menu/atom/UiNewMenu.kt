package com.dirtfy.ppp.ui.state.feature.menu.atom

data class UiNewMenu(
    val name: String = "",
    val price: String = "",
    val isAlcohol: Boolean = false,
    val isLunch: Boolean = false,
    val isDinner: Boolean = false,
)
