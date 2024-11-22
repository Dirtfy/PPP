package com.dirtfy.ppp.ui.controller.feature.menu

import com.dirtfy.ppp.ui.state.feature.menu.UiMenuUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import kotlinx.coroutines.flow.Flow

interface MenuUpdateController {

    val screenData: Flow<UiMenuUpdateScreenState>

    fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu(menu: UiMenu)
    suspend fun deleteMenu(menu: UiMenu)
}