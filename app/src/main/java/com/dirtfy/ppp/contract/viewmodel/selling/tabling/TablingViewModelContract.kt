package com.dirtfy.ppp.contract.viewmodel.selling.tabling

import android.util.Log
import androidx.compose.runtime.State
import com.dirtfy.tagger.Tagger

object TablingViewModelContract {

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

    object MenuList {
        interface API {
            val menuList: State<List<DTO.Menu>>

            fun checkMenu()

            fun orderMenu(menu: DTO.Menu, tableNumber: Int)
            fun cancelMenu(menu: DTO.Menu, tableNumber: Int)
        }
    }

    object OrderList {
        interface API {
            val orderList: State<List<DTO.Order>>
            val total: State<DTO.Total>


            fun checkOrder(tableNumber: Int)

            fun cancelMenu(menu: DTO.Menu)

            fun payTable(
                tableNumber: Int,
                payment: DTO.Payment,
                pointAccountNumber: String? = null
            )
        }
    }

    object TableList {
        interface API {
            val tableList: State<List<DTO.Table>>
            val selectedTableNumber: State<Int>

            fun checkTables()

            fun selectTable(tableNumber: Int)
            fun deselectTable(tableNumber: Int)
            fun mergeTable(baseTableNumber: Int, mergedTableNumber: Int)
            fun cleanTable(tableNumber: Int)
        }
    }

    interface API: MenuList.API, OrderList.API, TableList.API, Tagger {

        fun orderMenu(menu: DTO.Menu) {
            Log.d(TAG, "${selectedTableNumber.value}")
            orderMenu(menu, selectedTableNumber.value)
        }

        fun checkOrder() {
            checkOrder(selectedTableNumber.value)
        }

        fun payTable(
            payment: DTO.Payment,
            pointAccountNumber: String?
        ) {
            payTable(selectedTableNumber.value, payment, pointAccountNumber)
        }
    }
}