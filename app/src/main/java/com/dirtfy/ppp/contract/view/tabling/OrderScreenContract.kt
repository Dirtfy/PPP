package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.contract.viewmodel.user.User

object OrderScreenContract {

    interface API {

        @Composable
        fun OrderList(
            orderList: List<TablingContract.DTO.Order>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun OrderItem(
            order: TablingContract.DTO.Order,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Total(
            total: TablingContract.DTO.Total,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun PaymentSelector(
            user: User,
            modifier: Modifier
        )

    }
}