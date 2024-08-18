package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

object DummyTablingViewModel: TablingViewModelContract.API {
    override fun orderMenu(menu: TablingViewModelContract.DTO.Menu, tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun checkOrder(tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun payTable(
        tableNumber: Int,
        payment: TablingViewModelContract.DTO.Payment,
        pointAccountNumber: String?
    ) {
        TODO("Not yet implemented")
    }

    override val menuList: State<List<TablingViewModelContract.DTO.Menu>>
        get() = TODO("Not yet implemented")

    override fun checkMenu() {
        TODO("Not yet implemented")
    }

    override fun cancelMenu(menu: TablingViewModelContract.DTO.Menu, tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun cancelMenu(menu: TablingViewModelContract.DTO.Menu) {
        TODO("Not yet implemented")
    }

    override val orderList: State<List<TablingViewModelContract.DTO.Order>>
        get() = TODO("Not yet implemented")
    override val total: State<TablingViewModelContract.DTO.Total>
        get() = TODO("Not yet implemented")
    override val tableList: State<List<TablingViewModelContract.DTO.Table>>
        get() = TODO("Not yet implemented")
    override val selectedTableNumber: State<Int>
        get() = TODO("Not yet implemented")

    override fun checkTables() {
        TODO("Not yet implemented")
    }

    override fun selectTable(tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun deselectTable(tableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun mergeTable(baseTableNumber: Int, mergedTableNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun cleanTable(tableNumber: Int) {
        TODO("Not yet implemented")
    }
}