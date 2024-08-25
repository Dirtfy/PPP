package com.dirtfy.ppp.ui.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.MenuService
import com.dirtfy.ppp.data.logic.TableService
import com.dirtfy.ppp.data.source.firestore.menu.MenuFireStore
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.data.source.firestore.table.TableFireStore
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiMenu.Companion.convertToUiMenu
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.TableController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class TableViewModel: ViewModel(), TableController {

    private val tableService: TableService = TableService(TableFireStore(), RecordFireStore())
    private val menuService: MenuService = MenuService(MenuFireStore())

    private val _tableList: MutableStateFlow<FlowState<List<UiTable>>>
    = MutableStateFlow(FlowState.loading())
    override val tableList: StateFlow<FlowState<List<UiTable>>>
        get() = _tableList

    private val _orderList: MutableStateFlow<FlowState<List<UiTableOrder>>>
    = MutableStateFlow(FlowState.loading())
    override val orderList: StateFlow<FlowState<List<UiTableOrder>>>
        get() = _orderList

    private val _menuList: MutableStateFlow<FlowState<List<UiMenu>>>
    = MutableStateFlow(FlowState.loading())
    override val menuList: StateFlow<FlowState<List<UiMenu>>>
        get() = _menuList

    private val _orderTotalPrice: MutableStateFlow<String>
    = MutableStateFlow("")
    override val orderTotalPrice: StateFlow<String>
        get() = _orderTotalPrice

    private val _pointUse: MutableStateFlow<UiPointUse>
    = MutableStateFlow(UiPointUse("",""))
    override val pointUse: StateFlow<UiPointUse>
        get() = _pointUse

    override fun updateTableList() {
        TODO("Not yet implemented")
    }

    override fun updateOrderList(table: UiTable) {
        TODO("Not yet implemented")
    }

    private suspend fun _updateMenuList() {
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                data.map { menu -> menu.convertToUiMenu() }
            }
        }
    }
    override fun updateMenuList() = request {
        _updateMenuList()
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        _pointUse.value = pointUse
    }

    override fun clickTable(table: UiTable) {
        TODO("Not yet implemented")
    }

    override fun mergeTable() {
        TODO("Not yet implemented")
    }

    override fun payTableWithCash() {
        TODO("Not yet implemented")
    }

    override fun payTableWithCard() {
        TODO("Not yet implemented")
    }

    override fun payTableWithPoint() {
        TODO("Not yet implemented")
    }

    override fun addOrder(menuName: String) {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(menuName: String) {
        TODO("Not yet implemented")
    }

    private fun request(job: suspend TableViewModel.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}