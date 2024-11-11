package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTableOrder
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.UiTableOrderScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TableOrderControllerImpl @Inject constructor(
    private val tableBusinessLogic: TableBusinessLogic
): TableOrderController, Tagger {

    private lateinit var orderListFlow: Flow<List<UiTableOrder>>

    private val _screenData: MutableStateFlow<UiTableOrderScreenState>
        = MutableStateFlow(UiTableOrderScreenState())
    override val screenData: Flow<UiTableOrderScreenState>
        get() = _screenData

    private suspend fun _updateOrderList(tableNumber: Int) {
        _screenData.update {
            it.copy(
                orderListState = UiScreenState(UiState.LOADING),
                orderTotalPrice = StringFormatConverter.formatCurrency(0)
            )
        }

        orderListFlow = tableBusinessLogic.orderStream(tableNumber).map { it.map { data -> data.convertToUiTableOrder() } }

        orderListFlow
            .catch { cause ->
                Log.e(TAG, "orderList load failed")
                _screenData.update {
                    it.copy(
                        orderListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .conflate().collect {
                _screenData.update { before ->
                    before.copy(
                        orderList = it,
                        orderListState = UiScreenState(UiState.COMPLETE),
                        orderTotalPrice = StringFormatConverter.formatCurrency(
                            it.sumOf { order -> StringFormatConverter.parseCurrency(order.price) * order.count.toInt() }
                        )
                    )
                }
            }
    }
    override suspend fun updateOrderList(table: UiTable) {
        val tableNumber = table.number.toInt()
        _updateOrderList(tableNumber)
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        _screenData.update { it.copy(pointUse = pointUse) }
    }

    private suspend fun _payTableWithCash(
        tableNumber: Int
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithCash(tableNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
            }
    }
    override suspend fun payTableWithCash(tableNumber: Int) {
        _payTableWithCash(tableNumber)
    }

    private suspend fun _payTableWithCard(
        tableNumber: Int
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithCard(tableNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
            }
    }
    override suspend fun payTableWithCard(tableNumber: Int) {
        _payTableWithCard(tableNumber)
    }

    private suspend fun _payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithPoint(
            tableNumber = tableNumber,
            accountNumber = accountNumber,
            issuedName = issuedName
        ).catch { cause ->
            _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect {
            _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun payTableWithPoint(tableNumber: Int) {
        val accountNumber = _screenData.value.pointUse.accountNumber.toInt()
        val issuedName = _screenData.value.pointUse.userName
        _payTableWithPoint(
            tableNumber,
            accountNumber,
            issuedName
        )
    }

    private suspend fun _addOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) {
        Log.d("WeGlonD", "viewmodel addorder")
        _screenData.update { it.copy(addOrderState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.addOrder(
            tableNumber, menuName, menuPrice
        ).catch { cause ->
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect { data ->
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun addOrder(tableNumber: Int, name: String, price: String) {
        val menuPrice = StringFormatConverter.parseCurrency(price)
        _addOrder(tableNumber, name, menuPrice)
    }

    private suspend fun _cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) {
        _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.cancelOrder(
            tableNumber, menuName, menuPrice
        ).catch { cause ->
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect {
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun cancelOrder(tableNumber: Int, name: String, price: String) {
        val menuPrice = StringFormatConverter.parseCurrency(price)
        _cancelOrder(tableNumber, name, menuPrice)
    }

}