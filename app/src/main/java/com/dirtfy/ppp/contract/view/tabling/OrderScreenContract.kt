package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object OrderScreenContract {

    object DTO {

        data class Order(
            val name: String,
            val price: String,
            val count: String
        )

        data class Total(
            val price: String
        )

        enum class Payment {
            Card,
            Cash,
            Point
        }

    }

    interface API {

        @Composable
        fun OrderList(
            orderList: List<DTO.Order>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun OrderItem(
            order: DTO.Order,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Total(
            total: DTO.Total,
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