package com.dirtfy.ppp.ui.holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.menu.MenuService
import com.dirtfy.ppp.data.logic.menu.ServiceMenu
import com.dirtfy.ppp.data.source.MenuFireStore
import com.dirtfy.ppp.ui.holder.menu.HolderMenu
import com.dirtfy.ppp.ui.holder.menu.MenuHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MenuViewModel: ViewModel(), MenuHolder {

    private val menuService = MenuService(MenuFireStore())

    private val _menuList: MutableStateFlow<FlowState<List<HolderMenu>>>
    = MutableStateFlow(FlowState.loading())
    private var _menuListLastValue: List<HolderMenu>
    = listOf()
    override val menuList: StateFlow<FlowState<List<HolderMenu>>>
        get() =_menuList

    private val _newMenu: MutableStateFlow<HolderMenu>
    = MutableStateFlow(HolderMenu("", ""))
    override val newMenu: StateFlow<HolderMenu>
        get() = _newMenu

    private fun ServiceMenu.convertToViewModelMenu(): HolderMenu {
        return HolderMenu(
            name = name,
            price = DecimalFormat("#,###").format(price),
        )
    }
    private fun HolderMenu.convertToServiceMenu(): ServiceMenu {
        return ServiceMenu(
            name = name,
            price = price.split(",").joinToString(separator = "").toInt(),
        )
    }

    override suspend fun updateMenuList() {
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                val newValue = data.map { menu -> menu.convertToViewModelMenu() }

                _menuListLastValue = newValue
                newValue
            }
        }
    }

    override suspend fun updateNewMenu(menu: HolderMenu) {
        _newMenu.value = menu
    }

    override suspend fun createMenu(menu: HolderMenu) {
        menuService.createMenu(
            menu.convertToServiceMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { newData ->
                val nowList = _menuListLastValue.toMutableList()

                nowList.add(newData.convertToViewModelMenu())

                _menuListLastValue = nowList
                nowList
            }

        }
    }

    override suspend fun deleteMenu(menu: HolderMenu) {
        menuService.deleteMenu(
            menu.convertToServiceMenu()
        ).conflate().collect {
            _menuList.value = it.passMap { data ->
                val nowList = _menuListLastValue.toMutableList()
                nowList.removeIf { target ->
                    target == data.convertToViewModelMenu()
                }

                _menuListLastValue = nowList
                nowList
            }
        }
    }

    override fun request(job: suspend MenuHolder.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}