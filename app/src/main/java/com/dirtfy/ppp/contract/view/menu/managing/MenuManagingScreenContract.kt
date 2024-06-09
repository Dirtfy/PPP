package com.dirtfy.ppp.contract.view.menu.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.MenuManagingContract

object MenuManagingScreenContract {

    interface API {

        @Composable
        fun MenuList(
            menuList: List<MenuManagingContract.DTO.Menu>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun MenuItem(
            menu: MenuManagingContract.DTO.Menu,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun MenuAdd(
            menu: MenuManagingContract.DTO.Menu,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: MenuManagingContract.API,
            user: User,
            modifier: Modifier
        )

    }

}