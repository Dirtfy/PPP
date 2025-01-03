package com.dirtfy.ppp.ui.view.tablet.table

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import com.dirtfy.ppp.ui.view.common.Component
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject

class TableScreen @Inject constructor(
    private val tableController: TableController
){

    @Composable
    fun Main(
        controller: TableController = tableController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateTableList()
        }

        Component.HandleUiStateDialog(
            uiState = screenData.trySetMergeModeState,
            onDismissRequest = { controller.setTrySetMergeModeState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { trySetMergeMode() } }
        )
        Component.HandleUiStateDialog(
            uiState = screenData.escapeFromMergeModeState,
            onDismissRequest = { controller.setEscapeFromMergeModeState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { escapeFromMergeMode() } }
        )
        Component.HandleUiStateDialog(
            uiState = screenData.mergeTableState,
            onDismissRequest = { controller.setMergeTableState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = {controller.request { mergeTable() } }
        )

        Component.HandleUiStateDialog(
            uiState = screenData.payTableWithCashState,
            onDismissRequest = { controller.setPayTableWithCashState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { payTableWithCash() } }
        )
        Component.HandleUiStateDialog(
            uiState = screenData.payTableWithCardState,
            onDismissRequest = { controller.setPayTableWithCardState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { payTableWithCard() } }
        )
        Component.HandleUiStateDialog(
            uiState = screenData.payTableWithPointState,
            onDismissRequest = { controller.setPayTableWithPointState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { payTableWithPoint() } }
        )

        Component.HandleUiStateDialog(
            uiState = screenData.addOrderState,
            onDismissRequest = { controller.setAddOrderState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = null
        )

        Component.HandleUiStateDialog(
            uiState = screenData.cancelOrderState,
            onDismissRequest = { controller.setCancelOrderState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = null
        )


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
            timeLeftUntilEndOfMergeMode = screenData.timeLeftUntilEndOfMergeMode,
            addOrderState = screenData.addOrderState,
            cancelOrderState = screenData.cancelOrderState,
            onTableClick = {
                controller.request {
                    controller.clickTable(it)
                    updateOrderList(it)
                    updateMenuList()
                }
            },
            onMergeClick = { controller.request { trySetMergeMode() } },
            onMergeOkClick = { controller.request { mergeTable() } },
            onMergeCancelClick = { controller.request { escapeFromMergeMode() } },
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
            },
            onMenuListFailDismissRequest = {
                controller.setMenuListState(UiScreenState(UiState.COMPLETE))
            },
            onMenuListFailRetryClick = {
                controller.retryUpdateTableList()
            },
            onTableListFailDismissRequest = {
                controller.setTableListState(UiScreenState(UiState.COMPLETE))
            },
            onTableListFailRetryClick = {
                controller.retryUpdateTableList()
            },
            onOrderListFailDismissRequest = {
                // TODO 이렇게 호출하는 함수가 TableController 내부에 있으면 더 좋을 듯 initOrderListFlow 같은 느낌
                controller.updateOrderList(UiTable(number = "0", color = Color.Transparent.value))

                controller.setMode(UiTableMode.Main)
            },
            onOrderListFailRetryClick = {
                controller.retryUpdateOrderList()
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
        timeLeftUntilEndOfMergeMode: String,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onMergeClick : () -> Unit,
        onMergeOkClick: () -> Unit,
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
        onPointUseConfirm: () -> Unit,
        onMenuListFailDismissRequest: () -> Unit,
        onMenuListFailRetryClick: () -> Unit,
        onTableListFailDismissRequest: () -> Unit,
        onTableListFailRetryClick: () -> Unit,
        onOrderListFailDismissRequest: () -> Unit,
        onOrderListFailRetryClick: () -> Unit,
    ) {
        ConstraintLayout {
            val (table, order, menu) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(table) {
                        start.linkTo(parent.start)
                        if (mode == UiTableMode.Order || mode == UiTableMode.PointUse) {
                            end.linkTo(menu.start)
                        } else {
                            end.linkTo(parent.end)
                        }
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                    }
            ) {
                Component.HandleUiStateDialog(
                    uiState = tableListState,
                    onDismissRequest = onTableListFailDismissRequest, onRetryAction = onTableListFailRetryClick,
                    onComplete = {
                        TableLayout(
                            tableList = tableList,
                            mode = mode,
                            timeLeftUntilEndOfMergeMode = timeLeftUntilEndOfMergeMode,
                            onTableClick = onTableClick,
                            onMergeClick = onMergeClick,
                            onMergeOkClick = onMergeOkClick,
                            onMergeCancelClick = onMergeCancelClick
                        )
                    }
                )
            }

            if (mode == UiTableMode.Order || mode == UiTableMode.PointUse) {
                Box(
                    modifier = Modifier.constrainAs(order) {
                        start.linkTo(parent.start)
                        end.linkTo(menu.start)

                        top.linkTo(table.bottom)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    Component.HandleUiStateDialog(
                        uiState = tableOrderListState,
                        onDismissRequest = onOrderListFailDismissRequest,
                        onRetryAction = onOrderListFailRetryClick,
                        onComplete = {
                            OrderLayout(
                                tableOrderList = tableOrderList,
                                totalPrice = totalPrice,
                                addOrderState = addOrderState,
                                cancelOrderState = cancelOrderState,
                                onCardClick = onCardClick,
                                onCashClick = onCashClick,
                                onPointClick = onPointClick,
                                onAddClick = onAddClick,
                                onCancelClick = onCancelClick
                            )
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .constrainAs(menu) {
                            end.linkTo(parent.end)
                        }
                ) {
                    Component.HandleUiStateDialog(
                        uiState = menuListState,
                        onDismissRequest = onMenuListFailDismissRequest, onRetryAction = onMenuListFailRetryClick,
                        onComplete = {
                            MenuList(
                                menuList = menuList,
                                addOrderState = addOrderState,
                                cancelOrderState = cancelOrderState,
                                onAddClick = onMenuAddClick,
                                onCancelClick = onMenuCancelClick
                            )
                        }
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
    fun TableLayout(
        tableList: List<UiTable>,
        mode: UiTableMode,
        timeLeftUntilEndOfMergeMode: String,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit,
        onMergeOkClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        Box{
            ConstraintLayout(modifier = Modifier.padding(10.dp)) {
                val (tables, mergeButtonRow) = createRefs()

                Box(
                    modifier = Modifier
                        .constrainAs(tables){
                            top.linkTo(parent.top)
                        }
                ){
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(10)
                    ) {
                        items(tableList) {
                            Box(
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Table(
                                    table = it,
                                    onClick = { onTableClick(it) }
                                )
                            }
                        }
                    }
                }
                Box(modifier = Modifier
                    .constrainAs(mergeButtonRow){
                        bottom.linkTo(tables.bottom)
                    }
                ) {
                    MergeButtonLayout(
                        mode = mode,
                        timeLeftUntilEndOfMergeMode = timeLeftUntilEndOfMergeMode,
                        onMergeClick = onMergeClick,
                        onMergeOkClick = onMergeOkClick,
                        onMergeCancelClick = onMergeCancelClick
                    )
                }
            }
        }
    }

    @Composable
    fun MergeButtonLayout(
        mode: UiTableMode,
        timeLeftUntilEndOfMergeMode: String,
        onMergeClick: () -> Unit,
        onMergeOkClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ){
        if (mode == UiTableMode.Merge) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.select_table), style = MaterialTheme.typography.bodyMedium)
                Text(text = timeLeftUntilEndOfMergeMode, style = MaterialTheme.typography.bodyMedium)
                Row {
                    Button(onClick = onMergeOkClick ) {
                        Text(text = stringResource(R.string.ok))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onMergeCancelClick ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }
        else{
            Row {
                Button(onClick = onMergeClick) {
                    Text(text = stringResource(R.string.merge))
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Table(
        table: UiTable,
        onClick: () -> Unit
    ){
        if (table.number != "0") {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(10.dp)
                    .background(
                        color = Color(table.color),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .combinedClickable(
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = table.number)
            }
        }
    }

    @Composable
    fun OrderLayout(
        tableOrderList: List<UiTableOrder>,
        totalPrice: String,
        addOrderState: UiScreenState,
        cancelOrderState: UiScreenState,
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout (
                modifier = Modifier.padding(10.dp)
            ) {
                val (order, total, pay) = createRefs()

                Box(
                    modifier = Modifier.constrainAs(order) {
                        top.linkTo(parent.top)
                        bottom.linkTo(pay.top)
                    }
                ) {
                    OrderList(
                        tableOrderList = tableOrderList,
                        onAddClick = onAddClick,
                        onCancelClick = onCancelClick
                    )
                }

                Row(
                    modifier = Modifier.constrainAs(total) {
                        top.linkTo(order.bottom)
                        bottom.linkTo(parent.bottom)

                        start.linkTo(parent.start)
                    }
                ) {
                    Text(text = stringResource(R.string.total), fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = totalPrice, fontSize = 20.sp)
                }

                Box(
                    modifier = Modifier.constrainAs(pay) {
                        bottom.linkTo(parent.bottom)

                        end.linkTo(parent.end)
                    }
                ) {
                    PaySelect(
                        onCardClick = onCardClick,
                        onCashClick = onCashClick,
                        onPointClick = onPointClick
                    )
                }
            }
        }

    }

    @Composable
    fun PaySelect(
        onCardClick: () -> Unit,
        onCashClick: () -> Unit,
        onPointClick: () -> Unit
    ) {
        Row {
            Button(onClick = onCashClick) {
                Text(text = stringResource(R.string.cash))
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = onCardClick) {
                Text(text = stringResource(R.string.card))
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = onPointClick) {
                Text(text = stringResource(R.string.point))
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
                        pointUse = pointUse,
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
        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            onAccountNumberChange(it.contents?:"")
        }

        Column {
            TextField(
                value = pointUse.accountNumber,
                onValueChange = onAccountNumberChange,
                label = { Text(text = stringResource(R.string.account_number)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    Component.BarcodeIcon {
                        scanLauncher.launch(
                            ScanOptions().setOrientationLocked(false)
                        )
                    }
                }
            )
            TextField(
                value = pointUse.userName,
                onValueChange = onUserNameChange,
                label = { Text(text = stringResource(R.string.issued_by)) }
            )
            Button(onClick = onConfirmClick) {
                Text(text = stringResource(R.string.ok))
            }
        }
    }

    @Composable
    fun OrderList(
        tableOrderList: List<UiTableOrder>,
        onAddClick: (UiTableOrder) -> Unit,
        onCancelClick: (UiTableOrder) -> Unit,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (tableOrderList.isNotEmpty()) {
                Column {
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
            } else {
                Text(
                    text= stringResource(R.string.empty_list), fontSize = 20.sp
                )
            }
        }

    }

    @Composable
    fun Order(
        tableOrder: UiTableOrder,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Card(
            modifier = Modifier.padding(10.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                val (name, price, count) = createRefs()

                Text(
                    text = tableOrder.name, fontSize = 25.sp,
                    modifier = Modifier.constrainAs(name) {
                        start.linkTo(parent.start)

                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )

                Text(
                    text = tableOrder.price, fontSize = 20.sp,
                    modifier = Modifier.constrainAs(price) {
                        end.linkTo(count.start)

                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )

                Row(
                    modifier = Modifier.constrainAs(count) {
                        end.linkTo(parent.end)

                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onAddClick) {
                        val addIcon = Icons.Filled.Add
                        Icon(imageVector = addIcon, contentDescription = addIcon.name)
                    }

                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = tableOrder.count, fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(10.dp))

                    IconButton(onClick = onCancelClick) {
                        val cancelIcon = Icons.Filled.Close
                        Icon(imageVector = cancelIcon, contentDescription = cancelIcon.name)
                    }
                }
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
        LazyColumn {
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
        Card(
            modifier = Modifier.width(400.dp)
        ) {
//            Row (
//                modifier = Modifier.padding(15.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                val (item, buttons) = createRefs()

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.constrainAs(item) {
                        start.linkTo(parent.start)
                    }
                ) {
                    Text(text = menu.name, fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = menu.price, fontSize = 16.sp)
                }

                Row(
                    modifier = Modifier.constrainAs(buttons) {
                        end.linkTo(parent.end)
                    }
                ) {
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
}