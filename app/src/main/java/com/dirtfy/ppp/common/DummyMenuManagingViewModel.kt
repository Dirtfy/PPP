package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import com.dirtfy.ppp.contract.viewmodel.selling.menu.managing.MenuManagingViewModelContract

object DummyMenuManagingViewModel: MenuManagingViewModelContract.API {
    override val newMenu: State<MenuManagingViewModelContract.DTO.Menu>
        get() = TODO("Not yet implemented")

    override fun addMenu() {
        TODO("Not yet implemented")
    }

    override fun setNewMenu(data: MenuManagingViewModelContract.DTO.Menu) {
        TODO("Not yet implemented")
    }

    override val menuList: State<List<MenuManagingViewModelContract.DTO.Menu>>
        get() = TODO("Not yet implemented")

    override fun checkMenuList() {
        TODO("Not yet implemented")
    }

    override fun deleteMenu(menu: MenuManagingViewModelContract.DTO.Menu) {
        TODO("Not yet implemented")
    }
}