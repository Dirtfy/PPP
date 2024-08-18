package com.dirtfy.ppp.contract.view.selling.menu.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.menu.managing.MenuManagingViewModelContract

object MenuManagingViewContract {

    interface API {

        @Composable
        fun MenuList(
            menuList: List<MenuManagingViewModelContract.DTO.Menu>,
            viewModel: MenuManagingViewModelContract.MenuList.API,
            modifier: Modifier
        )

        @Composable
        fun MenuItem(
            menu: MenuManagingViewModelContract.DTO.Menu,
            viewModel: MenuManagingViewModelContract.MenuList.API,
            modifier: Modifier
        )

        @Composable
        fun NewMenu(
            menu: MenuManagingViewModelContract.DTO.Menu,
            viewModel: MenuManagingViewModelContract.NewMenu.API,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: MenuManagingViewModelContract.API,
            modifier: Modifier
        )

    }

}