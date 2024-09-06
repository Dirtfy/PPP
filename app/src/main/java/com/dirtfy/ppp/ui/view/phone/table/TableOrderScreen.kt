package com.dirtfy.ppp.ui.view.phone.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.test.ui.view.phone.table.TableScreen
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTableOrder

object TableOrderScreen {

    @Composable
    fun Main(

    ) {

    }

    @Composable
    fun OrderLayoutState(
        tableOrderListState: FlowState<List<UiTableOrder>>,
        totalPrice: String,
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit
    ) {
        when(tableOrderListState) {
            is FlowState.Success -> {
                val orderList = tableOrderListState.data
                OrderLayout(
                    tableOrderList = orderList,
                    totalPrice = totalPrice,
                    onCardClick = onCardClick,
                    onCashClick = onCashClick,
                    onPointClick = onPointClick,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )
            }
            is FlowState.Failed -> {
                val throwable = tableOrderListState.throwable
                TableScreen.Fail(throwable = throwable) {

                }
            }
            is FlowState.Loading -> {
                TableScreen.Loading()
            }
        }
    }
    @Composable
    fun OrderLayout(
        tableOrderList: List<UiTableOrder>,
        totalPrice: String,
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit
    ) {
        Row {
            PaySelect(
                onCardClick = onCardClick,
                onCashClick = onCashClick,
                onPointClick = onPointClick
            )

            OrderList(
                tableOrderList = tableOrderList,
                totalPrice = totalPrice,
                onAddClick = onAddClick,
                onCancelClick = onCancelClick
            )
        }
    }

    @Composable
    fun PaySelect(
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit
    ) {
        Column {
            Button(onClick = onCashClick) {
                Text(text = "Cash")
            }
            Button(onClick = onCardClick) {
                Text(text = "Card")
            }
            Button(onClick = onPointClick) {
                Text(text = "Point")
            }
        }
    }

    @Composable
    fun PointUseDataInputDialog(
        pointUse: UiPointUse,
        onDismissRequest: () -> Unit,
        onUserNameChange: (String) -> Unit,
        onAccountNumberChange: (String) -> Unit,
        onConfirmClick: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            PointUseDataInput(
                pointUse =pointUse,
                onAccountNumberChange = onAccountNumberChange,
                onUserNameChange = onUserNameChange,
                onConfirmClick = onConfirmClick
            )
        }
    }

    @Composable
    fun PointUseDataInput(
        pointUse: UiPointUse,
        onAccountNumberChange: (String) -> Unit,
        onUserNameChange: (String) -> Unit,
        onConfirmClick: () -> Unit
    ) {
        Column {
            TextField(
                value = pointUse.accountNumber,
                onValueChange = onAccountNumberChange
            )
            TextField(
                value = pointUse.userName,
                onValueChange = onUserNameChange
            )
            Button(onClick = onConfirmClick) {
                Text(text = "Confirm")
            }
        }
    }

    @Composable
    fun OrderList(
        tableOrderList: List<UiTableOrder>,
        totalPrice: String,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit,
    ) {
        Column {
            Row {
                Text(text = "total")
                Text(text = totalPrice)
            }

            LazyColumn {
                items(tableOrderList) {
                    Order(
                        tableOrder = it,
                        onAddClick = { onAddClick(it) },
                        onCancelClick = { onCancelClick(it) }
                    )
                }
            }
        }
    }

    @Composable
    fun Order(
        tableOrder: UiTableOrder,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Row {
            Text(text = tableOrder.name)
            Text(text = tableOrder.price)
            Text(text = tableOrder.count)
            IconButton(onClick = onAddClick) {
                val addIcon = Icons.Filled.Add
                Icon(imageVector = addIcon, contentDescription = addIcon.name)
            }
            IconButton(onClick = onCancelClick) {
                val cancelIcon = Icons.Filled.Close
                Icon(imageVector = cancelIcon, contentDescription = cancelIcon.name)
            }
        }
    }
}