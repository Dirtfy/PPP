package com.dirtfy.ppp.contract.viewmodel.selling.menu.managing

import androidx.compose.runtime.State

object MenuManagingViewModelContract {

    object DTO {
        data class Menu(
            val name: String,
            val price: String
        )
    }

    object NewMenu {
        interface API {
            val newMenu: State<DTO.Menu>

            fun addMenu()
            fun setNewMenu(data: DTO.Menu)
        }
    }

    object MenuList {
        interface API {
            val menuList: State<List<DTO.Menu>>

            fun checkMenuList()

            fun addMenu()
            fun deleteMenu(menu: DTO.Menu)
        }
    }

    interface API: NewMenu.API, MenuList.API
}