package com.dirtfy.ppp.ui.view.tablet.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import javax.inject.Inject

class TableScreen @Inject constructor(
    val tableController: TableController
) {

    @Composable
    fun Main(
        controller: TableController = tableController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateTableList()
        }

        ScreenContent(
            tableList = screenData.tableList,
            tableListState = screenData.tableListState,
            tableOrderList = screenData.orderList,
            tableOrderListState = screenData.orderListState,
            menuList = screenData.menuList,
            menuListState = screenData.menuListState,
            totalPrice = screenData.orderTotalPrice,
            pointUse = screenData.pointUse,
            mode = screenData.mode,
            mergeTableState = screenData.mergeTableState,
            payTableState = screenData.payTableState,
            addOrderState = screenData.addOrderState,
            cancelOrderState = screenData.cancelOrderState,
            onTableClick = {
                controller.request {
                    controller.clickTable(it)
                    updateOrderList(it)
                    updateMenuList()
                }
            },
            onTableLongClick = { controller.setMode(UiTableMode.Merge) },
            onMergeClick = { controller.request { mergeTable() } },
            onMergeCancelClick = { controller.cancelMergeTable() },
            onCashClick = { controller.request { payTableWithCash() } },
            onCardClick = { controller.request { payTableWithCard() } },
            onPointClick = { controller.setMode(UiTableMode.PointUse) },
            onAddClick = { controller.request { addOrder(it.name, it.price) } },
            onCancelClick = { controller.request { cancelOrder(it.name, it.price) } },
            onMenuAddClick = { controller.request { addOrder(it.name, it.price) } },
            onMenuCancelClick = { controller.request { cancelOrder(it.name, it.price) } },
            onPointUseDialogDismissRequest = { controller.setMode(UiTableMode.Order) },
            onPointUseUserNameChange = { controller.updatePointUse(screenData.pointUse.copy(userName = it)) },
            onPointUseAccountNumberChange = { controller.updatePointUse(screenData.pointUse.copy(accountNumber = it)) },
            onPointUseConfirm = {
                controller.request {
                    payTableWithPoint()
                    setMode(UiTableMode.Main)
                }
            }
        )
    }

    @Composable
    fun ScreenContent(
        tableList: List<UiTable>,
        tableListState: UiScreenState,
        tableOrderList: List<UiTableOrder>,
        tableOrderListState: UiScreenState,
        menuList: List<UiMenu>,
        menuListState: UiScreenState,
        totalPrice: String,
        pointUse: UiPointUse,
        mode: UiTableMode,
        mergeTableState: UiScreenState,
        payTableState: UiScreenState,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onTableLongClick: () -> Unit,
        onMergeClick: () -> Unit,
        onMergeCancelClick: () -> Unit,
        onCashClick: () -> Unit,
        onCardClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit,
        onMenuAddClick: (UiMenu) -> Unit,
        onMenuCancelClick: (UiMenu) -> Unit,
        onPointUseDialogDismissRequest: () -> Unit,
        onPointUseUserNameChange: (String) -> Unit,
        onPointUseAccountNumberChange: (String) -> Unit,
        onPointUseConfirm: () -> Unit
    ) {
        Column {
            TableLayoutState(
                tableList = tableList,
                tableListState = tableListState,
                mode = mode,
                mergeTableState = mergeTableState,
                onTableClick = onTableClick,
                onTableLongClick = onTableLongClick,
                onMergeClick = onMergeClick,
                onMergeCancelClick = onMergeCancelClick
            )

            if (mode == UiTableMode.Order || mode == UiTableMode.PointUse) {
                OrderLayoutState(
                    tableOrderList = tableOrderList,
                    tableOrderListState = tableOrderListState,
                    totalPrice = totalPrice,
                    payTableState = payTableState,
                    addOrderState = addOrderState,
                    cancelOrderState = cancelOrderState,
                    onCardClick = onCardClick,
                    onCashClick = onCashClick,
                    onPointClick = onPointClick,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )

                MenuListState(
                    menuList = menuList,
                    menuListState = menuListState,
                    addOrderState = addOrderState,
                    cancelOrderState = cancelOrderState,
                    onAddClick = onMenuAddClick,
                    onCancelClick = onMenuCancelClick
                )
            }

            if (mode == UiTableMode.PointUse) {
                PointUseDataInputDialog(
                    pointUse = pointUse,
                    payTableState = payTableState,
                    onDismissRequest = onPointUseDialogDismissRequest,
                    onUserNameChange = onPointUseUserNameChange,
                    onAccountNumberChange = onPointUseAccountNumberChange,
                    onConfirmClick = onPointUseConfirm,
                )
            }
        }
    }

    @Composable
    fun TableLayoutState(
        tableList: List<UiTable>,
        tableListState: UiScreenState,
        mode: UiTableMode,
        mergeTableState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onTableLongClick: () -> Unit,
        onMergeClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        when(tableListState.state) {
            UiState.COMPLETE -> {
                TableLayout(
                    tableList = tableList,
                    mode = mode,
                    mergeTableState = mergeTableState,
                    onTableClick = onTableClick,
                    onTableLongClick = onTableLongClick,
                    onMergeClick = onMergeClick,
                    onMergeCancelClick = onMergeCancelClick
                )
            }
            UiState.FAIL -> {
                Fail(failMessage = tableListState.failMessage) {

                }
            }
            UiState.LOADING -> {
                Loading()
            }
        }
    }

    @Composable
    fun TableLayout(
        tableList: List<UiTable>,
        mode: UiTableMode,
        mergeTableState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onTableLongClick: () -> Unit,
        onMergeClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(10)) {
                items(tableList) {
                    Table(
                        table = it,
                        onClick = { onTableClick(it) },
                        onLongClick = onTableLongClick
                    )
                }
            }

            if (mode == UiTableMode.Merge) {
                Row {
                    Button(onClick = onMergeClick) {
                        Text(text = "Merge")
                    }
                    Button(onClick = onMergeCancelClick) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Table(
        table: UiTable,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ){
        if (table.number != "0") {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .background(Color(table.color))
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
            ) {
                Text(text = table.number)
            }
        }
    }

    @Composable
    fun OrderLayoutState(
        tableOrderList: List<UiTableOrder>,
        tableOrderListState: UiScreenState,
        totalPrice: String,
        payTableState: UiScreenState,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit
    ) {
        when(tableOrderListState.state) {
            UiState.COMPLETE -> {
                OrderLayout(
                    tableOrderList = tableOrderList,
                    totalPrice = totalPrice,
                    payTableState = payTableState,
                    addOrderState = addOrderState,
                    cancelOrderState = cancelOrderState,
                    onCardClick = onCardClick,
                    onCashClick = onCashClick,
                    onPointClick = onPointClick,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )
            }
            UiState.FAIL -> {
                Fail(failMessage = tableOrderListState.failMessage) {

                }
            }
            UiState.LOADING -> {
                Loading()
            }
        }
    }
    @Composable
    fun OrderLayout(
        tableOrderList: List<UiTableOrder>,
        totalPrice: String,
        payTableState: UiScreenState,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit
    ) {
        Row {
            PaySelect(
                payTableState = payTableState,
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
        payTableState: UiScreenState,
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
        payTableState: UiScreenState,
        onDismissRequest: () -> Unit,
        onUserNameChange: (String) -> Unit,
        onAccountNumberChange: (String) -> Unit,
        onConfirmClick: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier.wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PointUseDataInput(
                        pointUse =pointUse,
                        onAccountNumberChange = onAccountNumberChange,
                        onUserNameChange = onUserNameChange,
                        onConfirmClick = onConfirmClick
                    )
                }
            }
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
        menuList: List<UiMenu>,
        menuListState: UiScreenState,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onAddClick: (UiMenu) -> Unit,
        onCancelClick: (UiMenu) -> Unit
    ) {
        when(menuListState.state) {
            UiState.COMPLETE -> {
                MenuList(
                    menuList = menuList,
                    addOrderState = addOrderState,
                    cancelOrderState = cancelOrderState,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )
            }
            UiState.FAIL -> {
                Fail(failMessage = menuListState.failMessage) {

                }
            }
            UiState.LOADING -> {
                Loading()
            }
        }
    }
    @Composable
    fun MenuList(
        menuList: List<UiMenu>,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
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
        Column {
            Text(text = menu.name)
            Row {
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

    @Composable
    fun Loading() {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun Fail(
        failMessage: String?,
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
            title = { Text(text = failMessage ?: "unknown error") }
        )
    }
}