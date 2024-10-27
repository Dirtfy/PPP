package com.dirtfy.ppp.ui.controller.feature.menu

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu

interface MenuController: Controller<UiMenuScreenState, MenuController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()

    fun updateSearchClue(clue: String)
    fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu(menu: UiMenu)
    suspend fun deleteMenu(menu: UiMenu)

}