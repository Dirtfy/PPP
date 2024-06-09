package com.dirtfy.ppp.contract.viewmodel

import com.dirtfy.ppp.contract.user.selling.menu.managing.MenuManagingUser
import kotlinx.coroutines.flow.StateFlow

object MenuManagingContract {

    object DTO {
        data class Menu(
            val name: String,
            val price: String
        )
    }

    interface API: MenuManagingUser {
        val menuList: StateFlow<List<DTO.Menu>>
        val newMenu: StateFlow<DTO.Menu>
    }
}