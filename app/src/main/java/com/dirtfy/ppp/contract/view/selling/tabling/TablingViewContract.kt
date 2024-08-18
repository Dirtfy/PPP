package com.dirtfy.ppp.contract.view.selling.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

object TablingViewContract {

    interface API {

        @Composable
        fun Main(
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun InstantMenuCreation(
            menu: TablingViewModelContract.DTO.Menu,
            viewModel: TablingViewModelContract.MenuList.API,
            modifier: Modifier
        )

    }
}