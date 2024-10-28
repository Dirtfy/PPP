package com.dirtfy.ppp.ui.presenter.controller

import com.dirtfy.ppp.ui.dto.menu.UiMenu
import com.dirtfy.ppp.ui.dto.menu.screen.UiMenuScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller

interface MenuController: Controller<UiMenuScreenState, MenuController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()

    fun updateSearchClue(clue: String)
    fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu(menu: UiMenu)
    suspend fun deleteMenu(menu: UiMenu)

}