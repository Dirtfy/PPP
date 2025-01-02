package com.dirtfy.ppp.ui.controller.feature.menu

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import kotlinx.coroutines.flow.Flow

interface MenuUpdateController {

    val screenData: Flow<UiMenuUpdateScreenState>

    fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu()
    suspend fun deleteMenu(menu: UiMenu)
    fun setCreateMenuState(state: UiScreenState)
    fun setDeleteMenuState(state: UiScreenState)
}