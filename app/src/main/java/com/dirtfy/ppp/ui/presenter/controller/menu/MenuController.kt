package com.dirtfy.ppp.ui.presenter.controller.menu

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.presenter.controller.Controller
import kotlinx.coroutines.flow.StateFlow

interface MenuController: Controller {
    val menuList: StateFlow<FlowState<List<ControllerMenu>>>

    val newMenu: StateFlow<ControllerMenu>

    suspend fun updateMenuList()

    suspend fun updateNewMenu(menu: ControllerMenu)

    suspend fun createMenu(menu: ControllerMenu)

    suspend fun deleteMenu(menu: ControllerMenu)

    fun request(job: suspend MenuController.() -> Unit)
}