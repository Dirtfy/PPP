package com.dirtfy.ppp.contract.viewmodel

object TablingContract {
    object DTD {
        data class Menu(
            val name: String,
            val price: Int
        )

        data class Serve(
            val menu: Menu,
            val count: Int
        )

        data class Table(
            val color: Int,
            val order: List<Serve>
        )
    }

    object API {
        interface MenuList {

        }

        interface Order {

        }

        interface Table {

        }
    }

}