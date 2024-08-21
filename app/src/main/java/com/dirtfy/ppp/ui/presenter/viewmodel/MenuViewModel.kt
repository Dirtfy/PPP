package com.dirtfy.ppp.ui.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.menu.MenuException
import com.dirtfy.ppp.data.logic.menu.MenuService
import com.dirtfy.ppp.data.source.firestore.MenuFireStore
import com.dirtfy.ppp.ui.presenter.controller.menu.ControllerMenu
import com.dirtfy.ppp.ui.presenter.controller.menu.ControllerMenu.Companion.convertToControllerMenu
import com.dirtfy.ppp.ui.presenter.controller.menu.MenuController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel(), MenuController {

    private val menuService = MenuService(MenuFireStore())

    private val _menuList: MutableStateFlow<FlowState<List<ControllerMenu>>>
    = MutableStateFlow(FlowState.loading())
    private var _menuListLastValue: List<ControllerMenu>
    = emptyList()
    override val menuList: StateFlow<FlowState<List<ControllerMenu>>>
        get() =_menuList

    private val _newMenu: MutableStateFlow<ControllerMenu>
    = MutableStateFlow(ControllerMenu("", ""))
    override val newMenu: StateFlow<ControllerMenu>
        get() = _newMenu



    override suspend fun updateMenuList() {
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                val newValue = data.map { menu -> menu.convertToControllerMenu() }

                _menuListLastValue = newValue
                newValue
            }
        }
    }

    override suspend fun updateNewMenu(menu: ControllerMenu) {
        _newMenu.value = menu
    }

    override suspend fun createMenu(menu: ControllerMenu) {
        if (menu.name == "") {
            _menuList.value = FlowState.failed(MenuException.BlankName())
            return
        }
        if (menu.price == "") {
            _menuList.value = FlowState.failed(MenuException.BlankPrice())
            return
        }

        menuService.createMenu(
            menu.convertToServiceMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { newData ->
                val nowList = _menuListLastValue.toMutableList()

                nowList.add(newData.convertToControllerMenu())

                _menuListLastValue = nowList
                nowList
            }

        }
    }

    override suspend fun deleteMenu(menu: ControllerMenu) {
        menuService.deleteMenu(
            menu.convertToServiceMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { data ->
                val nowList = _menuListLastValue.toMutableList()
                nowList.removeIf { target ->
                    target == data.convertToControllerMenu()
                }

                _menuListLastValue = nowList
                nowList
            }
        }
    }

    override fun request(job: suspend MenuController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}