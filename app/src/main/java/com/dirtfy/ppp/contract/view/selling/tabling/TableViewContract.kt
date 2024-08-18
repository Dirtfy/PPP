package com.dirtfy.ppp.contract.view.selling.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

object TableViewContract {

    interface API {

        @Composable
        fun Table(
            table: TablingViewModelContract.DTO.Table,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun TableLayout(
            tableList: List<TablingViewModelContract.DTO.Table>,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

    }
}