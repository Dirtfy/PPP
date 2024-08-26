package com.dirtfy.ppp.ui.view.phone.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.TableController
import com.dirtfy.ppp.ui.presenter.viewmodel.TableViewModel

object TableScreen {

    @Composable
    fun Main(
        controller: TableController = viewModel<TableViewModel>()
    ) {
        val tableList by controller.tableList.collectAsStateWithLifecycle()
        val orderList by controller.orderList.collectAsStateWithLifecycle()
        val menuList by controller.menuList.collectAsStateWithLifecycle()
        val totalPrice by controller.orderTotalPrice.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateTableList()
        }

        ScreenContent(
            tableListState = tableList,
            tableOrderListState = orderList,
            menuListState = menuList,
            totalPrice = totalPrice,
            onTableClick = {
                controller.run {
                    clickTable(it)
                    updateOrderList(it)
                    updateMenuList()
                }
            },
            onMergeClick = { controller.mergeTable() },
            onCashClick = { controller.payTableWithCash() },
            onCardClick = { controller.payTableWithCard() },
            onPointClick = { controller.payTableWithPoint() },
            onAddClick = { controller.addOrder(it.name, it.price) },
            onCancelClick = { controller.cancelOrder(it.name, it.price) },
            onMenuAddClick = { controller.addOrder(it.name, it.price) },
            onMenuCancelClick = { controller.cancelOrder(it.name, it.price) }
        )
    }

    @Composable
    fun ScreenContent(
        tableListState: FlowState<List<UiTable>>,
        tableOrderListState: FlowState<List<UiTableOrder>>,
        menuListState: FlowState<List<UiMenu>>,
        totalPrice: String,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit,
        onCashClick: () -> Unit,
        onCardClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit,
        onMenuAddClick: (UiMenu) -> Unit,
        onMenuCancelClick: (UiMenu) -> Unit
    ) {
        Column {
            TableLayoutState(
                tableListState = tableListState,
                onTableClick = onTableClick,
                onMergeClick = onMergeClick
            )

            OrderLayoutState(
                tableOrderListState = tableOrderListState,
                totalPrice = totalPrice,
                onCardClick = onCardClick,
                onCashClick = onCashClick,
                onPointClick = onPointClick,
                onAddClick = onAddClick,
                onCancelClick = onCancelClick
            )

            MenuListState(
                menuListState = menuListState,
                onAddClick = onMenuAddClick,
                onCancelClick = onMenuCancelClick
            )
        }

    }

    @Composable
    fun TableLayoutState(
        tableListState: FlowState<List<UiTable>>,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit
    ) {
        when(tableListState) {
            is FlowState.Success -> {
                val tableList = tableListState.data
                TableLayout(
                    tableList = tableList,
                    onTableClick = onTableClick,
                    onMergeClick = onMergeClick
                )
            }
            is FlowState.Failed -> {
                val throwable = tableListState.throwable
                Fail(throwable = throwable) {

                }
            }
            is FlowState.Loading -> {
                Loading()
            }
        }
    }

    @Composable
    fun TableLayout(
        tableList: List<UiTable>,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(10)) {
                items(tableList) {
                    Table(
                        table = it,
                        onClick = { onTableClick(it) }
                    )
                }
            }
            Button(onClick = onMergeClick) {
                Text(text = "Merge")
            }
        }
    }

    @Composable
    fun Table(
        table: UiTable,
        onClick: () -> Unit
    ){
        if (table.number != "0") {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .background(Color(table.color))
                    .clickable {
                        onClick()
                    }
            ) {
                Text(text = table.number)
            }
        }
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
                Fail(throwable = throwable) {

                }
            }
            is FlowState.Loading -> {
                Loading()
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

    @Composable
    fun MenuListState(
        menuListState: FlowState<List<UiMenu>>,
        onAddClick: (UiMenu) -> Unit,
        onCancelClick: (UiMenu) -> Unit
    ) {
        when(menuListState) {
            is FlowState.Success -> {
                val menuList = menuListState.data
                MenuList(
                    menuList = menuList,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )
            }
            is FlowState.Failed -> {
                val throwable = menuListState.throwable
                Fail(throwable = throwable) {

                }
            }
            is FlowState.Loading -> {
                Loading()
            }
        }
    }
    @Composable
    fun MenuList(
        menuList: List<UiMenu>,
        onAddClick: (UiMenu) -> Unit,
        onCancelClick: (UiMenu) -> Unit
    ) {
        LazyRow {
            items(menuList) {
                Menu(
                    menu = it,
                    onAddClick = { onAddClick(it) },
                    onCancelClick = { onCancelClick(it) }
                )
            }
        }
    }

    @Composable
    fun Menu(
        menu: UiMenu,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Text(text = menu.name)
    }

    @Composable
    fun Loading() {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun Fail(
        throwable: Throwable,
        onRetryClick: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = { }) {
                    Text(text = "Cancel")
                }
            },
            dismissButton = {
                Button(onClick = onRetryClick) {
                    Text(text = "Retry")
                }
            },
            title = { Text(text = throwable.message?: "unknown error") }
        )
    }
}