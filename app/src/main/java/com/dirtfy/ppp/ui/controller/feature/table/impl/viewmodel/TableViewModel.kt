package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.controller.feature.table.TableMergeController
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.state.feature.table.UiTableScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TableViewModel @Inject constructor(
    private val mergeController: TableMergeController,
    private val orderController: TableOrderController,
    private val menuController: TableMenuController
): ViewModel(), TableController, Tagger {

    private val modeFlow = MutableStateFlow(UiTableMode.Main)
    private var selectedTableNumber: Int = 0

    override val screenData: StateFlow<UiTableScreenState>
        = modeFlow
        .combine(menuController.screenData) { mode, menuScreenState ->
            UiTableScreenState(
                mode = mode,
                menuList = menuScreenState.menuList,
                menuListState = menuScreenState.menuListState
            )
        }.combine(mergeController.screenData) { state, mergeScreenData ->
            state.copy(
                tableList = mergeScreenData.tableList,
                sourceTableList = mergeScreenData.sourceTableList,
                tableListState = mergeScreenData.tableListState,
                mergeTableState = mergeScreenData.mergeTableState
            )
        }.combine(orderController.screenData) { state, orderScreenData ->
            state.copy(
                orderList = orderScreenData.orderList,
                orderTotalPrice = orderScreenData.orderTotalPrice,
                pointUse = orderScreenData.pointUse,
                orderListState = orderScreenData.orderListState,
                payTableState = orderScreenData.payTableState,
                addOrderState = orderScreenData.addOrderState,
                cancelOrderState = orderScreenData.cancelOrderState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiTableScreenState()
        )

    override suspend fun updateTableList() {
        mergeController.updateTableList()
    }

    override suspend fun updateOrderList(table: UiTable) {
        selectedTableNumber = table.number.toInt()
        println(selectedTableNumber.toString())
        orderController.updateOrderList(table)
    }

    override suspend fun updateMenuList() {
        menuController.updateMenuList()
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        orderController.updatePointUse(pointUse)
    }

    override fun clickTable(table: UiTable) {
        when (modeFlow.value) {
            UiTableMode.Merge -> mergeController.clickTableOnMergeMode(table)
            else -> {
                val selectedGroup = mergeController.clickTableOnMainOrOrderMode(table)
                if (selectedGroup > 0) setMode(UiTableMode.Order)
                else setMode(UiTableMode.Main)
            }
        }
    }

    override fun setMode(mode: UiTableMode) {
        modeFlow.update { mode }
    }

    override suspend fun mergeTable() {
        mergeController.mergeTable()
    }

    override fun cancelMergeTable() {
        mergeController.cancelMergeTable()
        setMode(UiTableMode.Main)
    }

    private fun disbandGroup() {
        mergeController.disbandGroup(selectedTableNumber)
        setMode(UiTableMode.Main)
    }

    override suspend fun payTableWithCash() {
        orderController.payTableWithCash(selectedTableNumber)
        disbandGroup()
    }

    override suspend fun payTableWithCard() {
        orderController.payTableWithCard(selectedTableNumber)
        disbandGroup()
    }

    override suspend fun payTableWithPoint() {
        orderController.payTableWithPoint(selectedTableNumber)
        disbandGroup()
    }

    override suspend fun addOrder(name: String, price: String) {
        orderController.addOrder(selectedTableNumber, name, price)
    }

    override suspend fun cancelOrder(name: String, price: String) {
        orderController.cancelOrder(selectedTableNumber, name, price)
    }

    override fun request(job: suspend TableController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}