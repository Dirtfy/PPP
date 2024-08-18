package com.dirtfy.ppp.viewmodel.use.selling.menu.managing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.contract.model.selling.MenuModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.menu.managing.MenuManagingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.menu.managing.MenuManagingViewModelContract.DTO.Menu
import com.dirtfy.ppp.model.selling.menu.managing.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel(), MenuManagingViewModelContract.API {

    private val menuModel: MenuModelContract.API = MenuRepository

    private val _newMenu: MutableState<Menu>
    = mutableStateOf(Menu(
        "",
        ""
    ))
    override val newMenu: State<Menu>
        get() = _newMenu

    private fun Menu.convertToModelMenu(): MenuModelContract.DTO.Menu {
        return MenuModelContract.DTO.Menu(
            null,
            this.name,
            this.price.toInt()
        )
    }
    private fun MenuModelContract.DTO.Menu.convertToViewModelMenu(): Menu {
        return Menu(
            this.name,
            this.price.toString()
        )
    }

    override fun addMenu() {
        viewModelScope.launch {
            val created = menuModel.create(data = _newMenu.value.convertToModelMenu())

            if (created.menuID != null) {
                val new = _menuList.value.toMutableList()
                new.add(created.convertToViewModelMenu())
                _menuList.value = new
            }
        }
    }

    private val _rawMenuList: MutableList<MenuModelContract.DTO.Menu>
    = mutableListOf()
    private val _menuList: MutableState<List<Menu>>
    = mutableStateOf(listOf())
    override val menuList: State<List<Menu>>
        get() = _menuList

    override fun checkMenuList() {
        viewModelScope.launch {
            _rawMenuList.clear()

            _rawMenuList.addAll(menuModel.read { true })

            _menuList.value = _rawMenuList.map { it.convertToViewModelMenu() }
        }
    }

    override fun deleteMenu(menu: Menu) {
        viewModelScope.launch {
            menuModel.delete {
                it.name == menu.name &&
                        it.price == menu.price.toInt()
            }

            _rawMenuList.removeIf {
                it.name == menu.name &&
                    it.price == menu.price.toInt()
            }
        }
    }

}