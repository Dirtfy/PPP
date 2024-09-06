package com.dirtfy.ppp.ui.presenter.viewmodel.table

import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.test.data.source.impl.firestore.record.RecordFireStore
import com.dirtfy.ppp.test.data.source.impl.firestore.table.TableFireStore
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableMode
import com.dirtfy.ppp.ui.dto.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.common.Utils
import com.dirtfy.ppp.ui.presenter.controller.table.TableOrderController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TableOrderViewModel: ViewModel(), TableOrderController, Tagger {

    private fun DataTableOrder.convertToUiTableOrder(): UiTableOrder {
        return UiTableOrder(
            name = name,
            price = Utils.currencyFormatting(price),
            count = count.toString()
        )
    }

    private val tableService =
        com.dirtfy.ppp.test.data.logic.impl.TableService(TableFireStore(), RecordFireStore())

    private val _orderList: MutableStateFlow<FlowState<List<UiTableOrder>>>
    = MutableStateFlow(FlowState.loading())
    override val orderList: StateFlow<FlowState<List<UiTableOrder>>>
        get() = _orderList

    private val _orderTotalPrice: MutableStateFlow<String>
    = MutableStateFlow("")
    override val orderTotalPrice: StateFlow<String>
        get() = _orderTotalPrice

    private val _pointUse: MutableStateFlow<UiPointUse>
    = MutableStateFlow(UiPointUse("",""))
    override val pointUse: StateFlow<UiPointUse>
        get() = _pointUse
    override val nowTable: StateFlow<Int>
        get() = TODO("Not yet implemented")

    private val _mode: MutableStateFlow<UiTableMode>
    = MutableStateFlow(UiTableMode.Order)
    override val mode: StateFlow<UiTableMode>
        get() = _mode

    override fun updateOrderList(table: UiTable) {
        TODO("Not yet implemented")
    }

    override fun updatePointUse(pointUse: UiPointUse) {
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

    override fun addOrder(name: String, price: String) {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(name: String, price: String) {
        TODO("Not yet implemented")
    }

    override fun setMode(mode: UiTableMode) {
        TODO("Not yet implemented")
    }
}