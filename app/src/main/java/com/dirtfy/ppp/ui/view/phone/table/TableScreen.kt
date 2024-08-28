package com.dirtfy.ppp.ui.view.phone.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableMode
import com.dirtfy.ppp.ui.dto.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.table.TableController
import com.dirtfy.ppp.ui.presenter.viewmodel.table.TableViewModel

object TableScreen {

    @Composable
    fun Main(
        controller: TableController = hiltViewModel<TableViewModel>()
    ) {
        val tableList by controller.tableList.collectAsStateWithLifecycle()
        val orderList by controller.orderList.collectAsStateWithLifecycle()
        val menuList by controller.menuList.collectAsStateWithLifecycle()
        val totalPrice by controller.orderTotalPrice.collectAsStateWithLifecycle()

        val pointUse by controller.pointUse.collectAsStateWithLifecycle()
        val mode by controller.mode.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateTableList()
        }

        ScreenContent(
            tableListState = tableList,
            tableOrderListState = orderList,
            menuListState = menuList,
            totalPrice = totalPrice,
            pointUse = pointUse,
            mode = mode,
            onTableClick = {
                controller.run {
                    clickTable(it)
                    updateOrderList(it)
                    updateMenuList()
                }
            },
            onTableLongClick = { controller.setMode(UiTableMode.Merge) },
            onMergeClick = { controller.mergeTable() },
            onMergeCancelClick = { controller.cancelMergeTable() },
            onCashClick = { controller.payTableWithCash() },
            onCardClick = { controller.payTableWithCard() },
            onPointClick = { controller.setMode(UiTableMode.PointUse) },
            onAddClick = { controller.addOrder(it.name, it.price) },
            onCancelClick = { controller.cancelOrder(it.name, it.price) },
            onMenuAddClick = { controller.addOrder(it.name, it.price) },
            onMenuCancelClick = { controller.cancelOrder(it.name, it.price) },
            onPointUseDialogDismissRequest = { controller.setMode(UiTableMode.Order) },
            onPointUseUserNameChange = { controller.updatePointUse(pointUse.copy(userName =  it)) },
            onPointUseAccountNumberChange = { controller.updatePointUse(pointUse.copy(accountNumber = it)) },
            onPointUseConfirm = {
                controller.run {
                    payTableWithPoint()
                    setMode(UiTableMode.Main)
                }
            }
        )
    }

    @Composable
    fun ScreenContent(
        tableListState: FlowState<List<UiTable>>,
        tableOrderListState: FlowState<List<UiTableOrder>>,
        menuListState: FlowState<List<UiMenu>>,
        totalPrice: String,
        pointUse: UiPointUse,
        mode: UiTableMode,
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
        ConstraintLayout {
            val (table, order, menu) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(table) {
                        top.linkTo(parent.top)
                    }
            ) {
                TableLayoutState(
                    tableListState = tableListState,
                    mode = mode,
                    onTableClick = onTableClick,
                    onTableLongClick = onTableLongClick,
                    onMergeClick = onMergeClick,
                    onMergeCancelClick = onMergeCancelClick
                )
            }

            if (mode == UiTableMode.Order || mode == UiTableMode.PointUse) {
                Box(
                    modifier = Modifier.constrainAs(order){
                        top.linkTo(table.bottom)
                        bottom.linkTo(menu.top)
                        height = Dimension.fillToConstraints
                    }
                ) {
                    OrderLayoutState(
                        tableOrderListState = tableOrderListState,
                        totalPrice = totalPrice,
                        onCardClick = onCardClick,
                        onCashClick = onCashClick,
                        onPointClick = onPointClick,
                        onAddClick = onAddClick,
                        onCancelClick = onCancelClick
                    )
                }

                Box(
                    modifier = Modifier
                        .constrainAs(menu) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    MenuListState(
                        menuListState = menuListState,
                        onAddClick = onMenuAddClick,
                        onCancelClick = onMenuCancelClick
                    )
                }
            }
        }

        if (mode == UiTableMode.PointUse) {
            PointUseDataInputDialog(
                pointUse = pointUse,
                onDismissRequest = onPointUseDialogDismissRequest,
                onUserNameChange = onPointUseUserNameChange,
                onAccountNumberChange = onPointUseAccountNumberChange,
                onConfirmClick = onPointUseConfirm,
            )
        }
    }

    @Composable
    fun TableLayoutState(
        tableListState: FlowState<List<UiTable>>,
        mode: UiTableMode,
        onTableClick: (UiTable) -> Unit,
        onTableLongClick: () -> Unit,
        onMergeClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        when(tableListState) {
            is FlowState.Success -> {
                val tableList = tableListState.data
                TableLayout(
                    tableList = tableList,
                    mode = mode,
                    onTableClick = onTableClick,
                    onTableLongClick = onTableLongClick,
                    onMergeClick = onMergeClick,
                    onMergeCancelClick = onMergeCancelClick
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
        mode: UiTableMode,
        onTableClick: (UiTable) -> Unit,
        onTableLongClick: () -> Unit,
        onMergeClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        Box {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10)
                ) {
                    items(tableList) {
                        Box(
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Table(
                                table = it,
                                onClick = { onTableClick(it) },
                                onLongClick = onTableLongClick
                            )
                        }
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
                    .background(
                        color = Color(table.color),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick
                    ),
                contentAlignment = Alignment.Center
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
        Box{
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                PaySelect(
                    onCardClick = onCardClick,
                    onCashClick = onCashClick,
                    onPointClick = onPointClick
                )

                Spacer(modifier = Modifier.size(10.dp))

                OrderList(
                    tableOrderList = tableOrderList,
                    totalPrice = totalPrice,
                    onAddClick = onAddClick,
                    onCancelClick = onCancelClick
                )
            }
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
                onValueChange = onAccountNumberChange,
                label = { Text(text = "account number") }
            )
            TextField(
                value = pointUse.userName,
                onValueChange = onUserNameChange,
                label = { Text(text = "issued name") }
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
                Text(text = "total:", fontSize = 20.sp)
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = totalPrice, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.size(15.dp))

            if (tableOrderList.isNotEmpty()) {
                Column {
                    Row {
                        Text(text = "name")
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = "price")
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = "count")
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
        }
    }

    @Composable
    fun Order(
        tableOrder: UiTableOrder,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = tableOrder.name)
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = tableOrder.price)
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = tableOrder.count)
            Spacer(modifier = Modifier.size(20.dp))
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
                Box(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Menu(
                        menu = it,
                        onAddClick = { onAddClick(it) },
                        onCancelClick = { onCancelClick(it) }
                    )
                }
            }
        }
    }

    @Composable
    fun Menu(
        menu: UiMenu,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Card{
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = menu.name, fontSize = 20.sp)
                Spacer(modifier = Modifier.size(10.dp))
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