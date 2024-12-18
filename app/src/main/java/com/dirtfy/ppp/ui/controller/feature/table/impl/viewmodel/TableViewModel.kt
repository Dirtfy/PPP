package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.controller.feature.table.TableListController
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TableViewModel @Inject constructor(
    private val listController: TableListController,
    private val orderController: TableOrderController,
    private val menuController: TableMenuController
): ViewModel(), TableController, Tagger {

    override val screenData: StateFlow<UiTableScreenState>
        = menuController.screenData
        .combine(listController.screenData) { menuScreenState, listScreenData ->
            UiTableScreenState(
                menuList = menuScreenState.menuList,
                menuListState = menuScreenState.menuListState,
                tableList = listScreenData.tableList,
                sourceTableList = listScreenData.sourceTableList,
                timeLeftUntilEndOfMergeMode = listScreenData.timeLeftUntilEndOfMergeMode,
                nowMenuCategory = menuScreenState.nowMenuCategory,
                mode = listScreenData.mode,
                tableListState = listScreenData.tableListState,
                trySetMergeModeState = listScreenData.trySetMergeModeState,
                escapeFromMergeModeState = listScreenData.escapeFromMergeModeState,
                mergeTableState = listScreenData.mergeTableState
            )
        }.combine(orderController.screenData) { state, orderScreenData ->
            state.copy(
                orderList = orderScreenData.orderList,
                orderTotalPrice = orderScreenData.orderTotalPrice,
                pointUse = orderScreenData.pointUse,
                orderListState = orderScreenData.orderListState,
                payTableWithCashState = orderScreenData.payTableWithCashState,
                payTableWithCardState = orderScreenData.payTableWithCardState,
                payTableWithPointState = orderScreenData.payTableWithPointState,
                addOrderState = orderScreenData.addOrderState,
                cancelOrderState = orderScreenData.cancelOrderState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiTableScreenState()
        )

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateTableList() {
        listController.updateTableList()
    }

    override fun retryUpdateTableList() {
        listController.retryUpdateTableList()
    }

    override fun updateOrderList(table: UiTable) {
        val groupNumber = listController.getGroupNumber(table.number.toInt())
        println("table${table.number} is group$groupNumber")
        orderController.updateOrderList(groupNumber)
    }

    override fun retryUpdateOrderList() {
        orderController.retryUpdateOrderList()
    }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateMenuList() {
        menuController.updateMenuList()
    }

    override fun retryUpdateMenuList() {
        menuController.retryUpdateMenuList()
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        orderController.updatePointUse(pointUse)
    }

    override fun setNowMenuCategory(category: MenuCategory) {
        menuController.setNowMenuCategory(category)
    }

    override fun clickTable(table: UiTable) {
        when (screenData.value.mode) {
            UiTableMode.Merge -> listController.clickTableOnMergeMode(table)
            else -> listController.clickTableOnMainOrOrderMode(table)
        }
    }

    override suspend fun trySetMergeMode() {
        listController.trySetMergeMode()
    }

    override suspend fun escapeFromMergeMode() {
        listController.escapeFromMergeMode()
    }

    override suspend fun mergeTable() {
        listController.mergeTable()
    }

    override fun cancelMergeTable() {
        listController.cancelMergeTable()
        setMode(UiTableMode.Main)
    }

    private fun disbandGroup() {
        //listController.disbandGroup(selectedTableNumber)
        listController.setMode(UiTableMode.Main)
    }

    override suspend fun payTableWithCash() {
        orderController.payTableWithCash()
        disbandGroup()
    }

    override suspend fun payTableWithCard() {
        orderController.payTableWithCard()
        disbandGroup()
    }

    override suspend fun payTableWithPoint() {
        orderController.payTableWithPoint()
        disbandGroup()
    }

    override suspend fun addOrder(name: String, price: String) {
        orderController.addOrder(name, price)
    }

    override suspend fun cancelOrder(name: String, price: String) {
        orderController.cancelOrder(name, price)
    }

    override fun setMode(mode: UiTableMode) {
        listController.setMode(mode)
    }

    override fun setMenuListState(state: UiScreenState){
        menuController.setMenuListState(state)
    }

    override fun setTableListState(state: UiScreenState) {
        listController.setTableListState(state)
    }

    override fun setTrySetMergeModeState(state: UiScreenState) {
        listController.setTrySetMergeModeState(state)
    }

    override fun setEscapeFromMergeModeState(state: UiScreenState) {
        listController.setEscapeFromMergeModeState(state)
    }

    override fun setMergeTableState(state: UiScreenState) {
        listController.setMergeTableState(state)
    }

    override fun setPayTableWithCashState(state: UiScreenState) {
        orderController.setPayTableWithCashState(state)
    }

    override fun setPayTableWithCardState(state: UiScreenState) {
        orderController.setPayTableWithCardState(state)
    }

    override fun setPayTableWithPointState(state: UiScreenState) {
        orderController.setPayTableWithPointState(state)
    }

    override fun setOrderListState(state: UiScreenState) {
        orderController.setOrderListState(state)
    }

    override fun setAddOrderState(state: UiScreenState) {
        orderController.setAddOrderState(state)
    }

    override fun setCancelOrderState(state: UiScreenState) {
        orderController.setCancelOrderState(state)
    }


    override fun request(job: suspend TableController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}