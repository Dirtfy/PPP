package com.dirtfy.ppp.ui.presenter.controller

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface MenuController: Controller {
    val menuList: StateFlow<FlowState<List<UiMenu>>>

    val newMenu: StateFlow<UiMenu>

    suspend fun updateMenuList()

    suspend fun updateNewMenu(menu: UiMenu)

    suspend fun createMenu(menu: UiMenu)

    suspend fun deleteMenu(menu: UiMenu)

    fun request(job: suspend MenuController.() -> Unit)
}