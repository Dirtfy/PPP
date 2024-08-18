package com.dirtfy.ppp.contract.view.selling.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

object MenuListViewContract {

    interface API {

        @Composable
        fun MenuList(
            menuList: List<TablingViewModelContract.DTO.Menu>,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun MenuItem(
            menu: TablingViewModelContract.DTO.Menu,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

    }

}