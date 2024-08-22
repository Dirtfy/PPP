package com.dirtfy.ppp.ui.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.logic.MenuService
import com.dirtfy.ppp.data.source.firestore.menu.MenuFireStore
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiMenu.Companion.convertToUiMenu
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel(), MenuController {

    private val menuService = MenuService(MenuFireStore())

    private val _menuList: MutableStateFlow<FlowState<List<UiMenu>>>
    = MutableStateFlow(FlowState.loading())
    private var _menuListLastValue: List<UiMenu>
    = emptyList()
    override val menuList: StateFlow<FlowState<List<UiMenu>>>
        get() =_menuList

    private val _newMenu: MutableStateFlow<UiMenu>
    = MutableStateFlow(UiMenu("", ""))
    override val newMenu: StateFlow<UiMenu>
        get() = _newMenu



    override suspend fun updateMenuList() {
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                val newValue = data.map { menu -> menu.convertToUiMenu() }

                _menuListLastValue = newValue
                newValue
            }
        }
    }

    override suspend fun updateNewMenu(menu: UiMenu) {
        _newMenu.value = menu
    }

    override suspend fun createMenu(menu: UiMenu) {
        if (menu.name == "") {
            _menuList.value = FlowState.failed(MenuException.BlankName())
            return
        }
        if (menu.price == "") {
            _menuList.value = FlowState.failed(MenuException.BlankPrice())
            return
        }

        menuService.createMenu(
            menu.convertToDataMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { newData ->
                val nowList = _menuListLastValue.toMutableList()

                nowList.add(newData.convertToUiMenu())

                _menuListLastValue = nowList
                nowList
            }

        }
    }

    override suspend fun deleteMenu(menu: UiMenu) {
        menuService.deleteMenu(
            menu.convertToDataMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { data ->
                val nowList = _menuListLastValue.toMutableList()
                nowList.removeIf { target ->
                    target == data.convertToUiMenu()
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