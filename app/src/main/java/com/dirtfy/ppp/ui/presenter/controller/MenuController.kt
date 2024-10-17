package com.dirtfy.ppp.ui.presenter.controller

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.menu.UiMenu
import com.dirtfy.ppp.ui.dto.menu.screen.UiMenuScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface MenuController: Controller {
    val uiMenuScreenState: StateFlow<UiMenuScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()

    fun updateSearchClue(clue: String)
    fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu(menu: UiMenu)
    suspend fun deleteMenu(menu: UiMenu)

    fun request(job: suspend MenuController.() -> Unit)
}