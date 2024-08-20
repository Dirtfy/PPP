package com.dirtfy.ppp.ui.holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.MenuService
import com.dirtfy.ppp.data.source.MenuFireStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MenuViewModel: ViewModel() {

    private val menuService = MenuService(MenuFireStore())

    data class Menu(
        val name: String,
        val price: String
    )

    private val _menuList: MutableStateFlow<FlowState<List<Menu>>>
    = MutableStateFlow(FlowState.loading())
    val menuList: StateFlow<FlowState<List<Menu>>>
        get() =_menuList

    private fun MenuService.Menu.convertToViewModelMenu(): Menu {
        return Menu(
            name = name,
            price = DecimalFormat("#,###").format(price),
        )
    }

    fun updateMenuList() {
        viewModelScope.launch {
            menuService.readMenu().collectLatest {
                _menuList.value = when(it) {
                    is FlowState.Loading -> FlowState.loading()
                    is FlowState.Success -> {
                        FlowState.success(
                            it.data.map { data ->
                                val serviceData = data as MenuService.Menu
                                serviceData.convertToViewModelMenu()
                            }
                        )
                    }
                    is FlowState.Failed -> {
                        FlowState.failed("?")
                    }
                }
            }
        }
    }
}