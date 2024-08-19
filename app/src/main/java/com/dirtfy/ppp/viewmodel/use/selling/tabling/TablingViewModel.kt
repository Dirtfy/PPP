package com.dirtfy.ppp.viewmodel.use.selling.tabling

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract

class TablingViewModel: ViewModel(), TablingViewModelContract.API {

    private val menuListViewModel: TablingViewModelContract.MenuList.API
            = TablingMenuListViewModel()
    private val orderListViewMode: TablingViewModelContract.OrderList.API
            = TablingOrderListViewModel()
    private val tableListViewModel: TablingViewModelContract.TableList.API
            = TablingTableListViewModel()

    override val menuList: State<List<TablingViewModelContract.DTO.Menu>>
        get() = menuListViewModel.menuList

    override fun checkMenu() {
        menuListViewModel.checkMenu()
    }

    override fun orderMenu(menu: TablingViewModelContract.DTO.Menu, tableNumber: Int) {
        menuListViewModel.orderMenu(menu, tableNumber)
    }

    override fun cancelMenu(menu: TablingViewModelContract.DTO.Menu, tableNumber: Int) {
        menuListViewModel.cancelMenu(menu, tableNumber)
    }

    override fun cancelMenu(menu: TablingViewModelContract.DTO.Menu) {
        menuListViewModel.cancelMenu(menu, selectedTableNumber.value)
        orderListViewMode.cancelMenu(menu)
    }

    override val orderList: State<List<TablingViewModelContract.DTO.Order>>
        get() = orderListViewMode.orderList
    override val total: State<TablingViewModelContract.DTO.Total>
        get() = orderListViewMode.total

    override fun checkOrder(tableNumber: Int) {
        orderListViewMode.checkOrder(tableNumber)
    }

    override val tableList: State<List<TablingViewModelContract.DTO.Table>>
        get() = tableListViewModel.tableList
    override val selectedTableNumber: State<Int>
        get() = tableListViewModel.selectedTableNumber

    override fun checkTables() {
        tableListViewModel.checkTables()
    }

    override fun selectTable(tableNumber: Int) {
        Log.d(TAG, "$tableNumber selected")
        tableListViewModel.selectTable(tableNumber)
    }

    override fun deselectTable(tableNumber: Int) {
        tableListViewModel.deselectTable(tableNumber)
    }

    override fun mergeTable(baseTableNumber: Int, mergedTableNumber: Int) {
        tableListViewModel.mergeTable(baseTableNumber, mergedTableNumber)
    }

    override fun cleanTable(tableNumber: Int) {
        tableListViewModel.cleanTable(tableNumber)
    }

    override fun payTable(
        tableNumber: Int,
        payment: TablingViewModelContract.DTO.Payment,
        pointAccountNumber: String?
    ) {
        orderListViewMode.payTable(
            tableNumber, payment, pointAccountNumber
        )
    }
}