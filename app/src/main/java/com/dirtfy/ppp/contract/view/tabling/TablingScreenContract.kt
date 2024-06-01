package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object TablingScreenContract {

    interface API {

        @Composable
        fun Main(
            orderList: List<OrderScreenContract.DTO.Order>,
            totalOrderData: OrderScreenContract.DTO.Total,
            tableList: List<TableScreenContract.DTO.Table>,
            menuList: List<MenuListScreenContract.DTO.Menu>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun InstantMenuCreation(
            user: User,
            modifier: Modifier
        )

    }
}