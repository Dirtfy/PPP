package com.dirtfy.ppp.ui.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.logic.service.MenuService
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiMenu.Companion.convertToUiMenu
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuService: MenuService
): ViewModel(), MenuController {

    private val _menuList: MutableStateFlow<FlowState<List<UiMenu>>>
    = MutableStateFlow(FlowState.loading())
    private var _menuListLastValue: List<UiMenu>
    = emptyList()
    override val menuList: StateFlow<FlowState<List<UiMenu>>>
        get() =_menuList

    private val _searchClue: MutableStateFlow<String>
    = MutableStateFlow("")
    override val searchClue: StateFlow<String>
        get() = _searchClue

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

    override suspend fun updateSearchClue(clue: String) {
        _searchClue.value = clue
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                val newValue = data.map { menu -> menu.convertToUiMenu() }

                val filteredList = newValue.filter { menu -> menu.name.contains(clue) }

                _menuListLastValue = filteredList
                filteredList
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