package com.dirtfy.ppp.contract.viewmodel

import com.dirtfy.ppp.contract.viewmodel.user.selling.tabling.TablingUser
import kotlinx.coroutines.flow.StateFlow

object TablingContract {

    object DTO {

        data class Table(
            val number: String,
            val color: ULong
        )

        data class Menu(
            val name: String,
            val price: String
        )

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

    interface API: TablingUser {
        val tableList: StateFlow<List<DTO.Table>>
        val menuList: StateFlow<List<DTO.Menu>>
        val orderList: StateFlow<List<DTO.Order>>
        val total: StateFlow<DTO.Total>
    }
}