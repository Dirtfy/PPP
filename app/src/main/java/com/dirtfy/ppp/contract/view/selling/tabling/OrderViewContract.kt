package com.dirtfy.ppp.contract.view.selling.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

object OrderViewContract {

    interface API {

        @Composable
        fun OrderList(
            orderList: List<TablingViewModelContract.DTO.Order>,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun OrderItem(
            order: TablingViewModelContract.DTO.Order,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun Total(
            total: TablingViewModelContract.DTO.Total,
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun PaymentSelector(
            viewModel: TablingViewModelContract.API,
            modifier: Modifier
        )

    }
}