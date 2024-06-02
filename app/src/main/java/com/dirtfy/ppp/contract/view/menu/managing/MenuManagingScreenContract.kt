package com.dirtfy.ppp.contract.view.menu.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object MenuManagingScreenContract {

    object DTO {

        data class Menu(
            val name: String,
            val price: String
        )

    }

    interface API {

        @Composable
        fun MenuList(
            menuList: List<DTO.Menu>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun MenuItem(
            menu: DTO.Menu,
            user: User,
            modifier: Modifier
        )

    }

}