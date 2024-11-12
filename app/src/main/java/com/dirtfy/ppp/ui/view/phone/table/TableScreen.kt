package com.dirtfy.ppp.ui.view.phone.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.common.exception.ExceptionRetryHandling
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.UiTableMergeScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import com.dirtfy.ppp.ui.view.phone.Component
import javax.inject.Inject

class TableScreen @Inject constructor(
    val tableController: TableController
){

    @Composable
    fun Main(
        controller: TableController = tableController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateTableList()
        }

        when (screenData.mergeTableState.state) {
            UiState.LOADING -> {
                Component.Loading()
            }
            UiState.COMPLETE -> {}
            UiState.FAIL -> {
                Component.Fail(
                    {controller.setMergeMode(UiScreenState(UiState.COMPLETE))},
                    screenData.mergeTableState.failMessage,
                    {
                        if(ExceptionRetryHandling.isTableRetry(screenData.mergeTableState.failMessage)){
                            controller.request { mergeTable() }
                        }
                    }
                )
            }
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
            onMergeClick = {controller.setMode(UiTableMode.Merge)},
            onMergeOkClick = { controller.request { mergeTable() } },
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
                    tableList = tableList,
                    tableListState = tableListState,
                    mode = mode,
                    mergeTableState = mergeTableState,
                    onTableClick = onTableClick,
                    onMergeClick = onMergeClick,
                    onMergeOkClick = onMergeOkClick,
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
                }

                Box(
                    modifier = Modifier
                        .constrainAs(menu) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    MenuListState(
                        menuList = menuList,
                        menuListState = menuListState,
                        addOrderState = addOrderState,
                        cancelOrderState = cancelOrderState,
                        onAddClick = onMenuAddClick,
                        onCancelClick = onMenuCancelClick
                    )
                }
            }
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

    @Composable
    fun TableLayoutState(
        tableList: List<UiTable>,
        tableListState: UiScreenState,
        mode: UiTableMode,
        mergeTableState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit,
        onMergeOkClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        when(tableListState.state) {
            UiState.COMPLETE -> {
                TableLayout(
                    tableList = tableList,
                    mode = mode,
                    mergeTableState = mergeTableState,
                    onTableClick = onTableClick,
                    onMergeClick = onMergeClick,
                    onMergeOkClick = onMergeOkClick,
                    onMergeCancelClick = onMergeCancelClick
                )
            }
            UiState.FAIL -> {
                //Component.Fail(failMessage = tableListState.failMessage,null)
                // TODO RetryCLick add or not
            }
            UiState.LOADING -> {
                Component.Loading()
            }
        }
    }

    @Composable
    fun TableLayout(
        tableList: List<UiTable>,
        mode: UiTableMode,
        mergeTableState: UiScreenState,
        onTableClick: (UiTable) -> Unit,
        onMergeClick: () -> Unit,
        onMergeOkClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ) {
        Box{
            ConstraintLayout(modifier = Modifier.padding(10.dp)) {
                val (tables,mergeButtonRow) = createRefs()
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
        onMergeClick: () -> Unit,
        onMergeOkClick: () -> Unit,
        onMergeCancelClick: () -> Unit
    ){
        if (mode == UiTableMode.Merge) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Please select a table", style = MaterialTheme.typography.bodyMedium)
                Row {
                    Button(onClick = onMergeOkClick ) {
                        Text(text = "OK")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onMergeCancelClick ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
        else{
            Row {
                Button(onClick = onMergeClick) {
                    Text(text = "Merge")
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
                    .size(35.dp)
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
                //Component.Fail(failMessage = tableOrderListState.failMessage,null)
                // TODO RetryCLick add or not
            }
            UiState.LOADING -> {
                Component.Loading()
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
        Box{
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                PaySelect(
                    payTableState = payTableState,
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
                //Component.Fail(failMessage = menuListState.failMessage,null)
                // TODO RetryCLick add or not
            }
            UiState.LOADING -> {
                Component.Loading()
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

    /*@Composable
    fun Loading() {
        Dialog(onDismissRequest = { *//*TODO*//* }) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
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
    }*/
}