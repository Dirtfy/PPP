package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTableOrder
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.UiTableOrderScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TableOrderControllerImpl @Inject constructor(
    private val tableBusinessLogic: TableBusinessLogic
): TableOrderController, Tagger {

    private val retryTrigger = MutableStateFlow(0)
    private val nowTableGroupFlow = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val orderStreamFlow: Flow<String>
        = retryTrigger
        .combine(nowTableGroupFlow) { retryCnt, nowGroupNumber ->
            Pair(retryCnt, nowGroupNumber)
        }.flatMapLatest { pair ->
            val groupNumber = pair.second
            if (groupNumber <= 0) {
                flow {
                    _screenData.update { it.copy(
                        orderList = emptyList(),
                        orderListState = UiScreenState(UiState.COMPLETE)
                    ) }
                    emit(StringFormatConverter.formatCurrency(0))
                }
            } else {
                tableBusinessLogic.orderStream(groupNumber).map {
                    val orderList = it.map { data -> data.convertToUiTableOrder() }
                    _screenData.update { before -> before.copy(
                        orderList = orderList,
                        orderListState = UiScreenState(UiState.COMPLETE)
                    ) }
                    StringFormatConverter.formatCurrency(
                        it.sumOf { order -> order.price * order.count }
                    )
                }.onStart {
                    _screenData.update { it.copy(
                        orderList = emptyList(),
                        orderListState = UiScreenState(UiState.LOADING)
                    ) }
                    emit(StringFormatConverter.formatCurrency(0))
                }.catch { cause ->
                    _screenData.update { it.copy(
                        orderList = emptyList(),
                        orderListState = UiScreenState(UiState.FAIL, cause)
                    ) }
                    emit(StringFormatConverter.formatCurrency(0))
                }
            }
        }

    private val _screenData: MutableStateFlow<UiTableOrderScreenState>
        = MutableStateFlow(UiTableOrderScreenState())
    override val screenData: Flow<UiTableOrderScreenState>
        = _screenData
        .combine(orderStreamFlow) { state, orderTotalPrice ->
            state.copy(
                orderTotalPrice = orderTotalPrice,
            )
        }

    override fun updateOrderList(groupNumber: Int) {
        nowTableGroupFlow.update { groupNumber }
    }

    override fun retryUpdateOrderList() {
        retryTrigger.value += 1
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        _screenData.update { it.copy(pointUse = pointUse) }
    }

    private suspend fun _payTableWithCash(
        groupNumber: Int
    ) {
        _screenData.update { it.copy(payTableWithCashState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithCash(groupNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableWithCashState = UiScreenState(UiState.FAIL, cause)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableWithCashState = UiScreenState(UiState.COMPLETE)) }
            }
    }
    override suspend fun payTableWithCash() {
        _payTableWithCash(nowTableGroupFlow.value)
    }

    private suspend fun _payTableWithCard(
        groupNumber: Int
    ) {
        _screenData.update { it.copy(payTableWithCardState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithCard(groupNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableWithCardState = UiScreenState(UiState.FAIL, cause)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableWithCardState = UiScreenState(UiState.COMPLETE)) }
            }
    }
    override suspend fun payTableWithCard() {
        _payTableWithCard(nowTableGroupFlow.value)
    }

    private suspend fun _payTableWithPoint(
        groupNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) {
        _screenData.update { it.copy(payTableWithPointState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.payTableWithPoint(
            groupNumber = groupNumber,
            accountNumber = accountNumber,
            issuedName = issuedName
        ).catch { cause ->
            _screenData.update { it.copy(payTableWithPointState = UiScreenState(UiState.FAIL, cause)) }
        }.conflate().collect {
            _screenData.update { it.copy(payTableWithPointState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun payTableWithPoint() {
        val accountNumber = _screenData.value.pointUse.accountNumber.toInt()
        val issuedName = _screenData.value.pointUse.userName
        _payTableWithPoint(
            nowTableGroupFlow.value,
            accountNumber,
            issuedName
        )
    }

    private suspend fun _addOrder(
        groupNumber: Int,
        menuName: String,
        menuPrice: Int,
        nowCount: UInt
    ) {
        Log.d("WeGlonD", "viewmodel addorder")
        _screenData.update { it.copy(addOrderState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.addOrder(
            groupNumber, menuName, menuPrice, nowCount
        ).catch { cause ->
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.FAIL, cause)) }
        }.conflate().collect {
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun addOrder(name: String, price: String) {
        val menuPrice = StringFormatConverter.parseCurrency(price)
        val order = _screenData.value.orderList.find { it.name == name }
        val count = order?.count?.toInt() ?: 0
        _addOrder(nowTableGroupFlow.value, name, menuPrice, count.toUInt())
    }

    private suspend fun _cancelOrder(
        groupNumber: Int,
        menuName: String,
        menuPrice: Int,
        nowCount: UInt
    ) {
        _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.cancelOrder(
            groupNumber, menuName, menuPrice, nowCount
        ).catch { cause ->
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.FAIL, cause)) }
        }.conflate().collect {
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun cancelOrder(name: String, price: String) {
        val menuPrice = StringFormatConverter.parseCurrency(price)
        val order = _screenData.value.orderList.find { it.name == name }
        val count = order?.count?.toInt() ?: 0
        _cancelOrder(nowTableGroupFlow.value, name, menuPrice, count.toUInt())
    }

    override fun setPayTableWithCashState(state: UiScreenState) {
        _screenData.update { it.copy(payTableWithCashState = state) }
    }

    override fun setPayTableWithCardState(state: UiScreenState) {
        _screenData.update { it.copy(payTableWithCardState = state) }
    }

    override fun setPayTableWithPointState(state: UiScreenState) {
        _screenData.update { it.copy(payTableWithPointState = state) }
    }

    override fun setOrderListState(state: UiScreenState) {
        _screenData.update { it.copy(orderListState = state) }
    }

    override fun setAddOrderState(state: UiScreenState) {
        _screenData.update { it.copy(addOrderState = state) }
    }

    override fun setCancelOrderState(state: UiScreenState) {
        _screenData.update { it.copy(cancelOrderState = state) }
    }

}