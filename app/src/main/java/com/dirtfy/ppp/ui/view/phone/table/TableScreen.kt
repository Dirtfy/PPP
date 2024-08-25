package com.dirtfy.ppp.ui.view.phone.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableOrder

object TableScreen {

    @Composable
    fun Main(
        tableList: List<UiTable>,
        tableOrderList: List<UiTableOrder>,
        menuList: List<UiMenu>,
        totalPrice: String,
        onTableClick: () -> Unit,
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
            TableLayout(
                tableList = tableList,
                onTableClick = onTableClick,
                onMergeClick = onMergeClick
            )

            OrderLayout(
                tableOrderList = tableOrderList,
                totalPrice = totalPrice,
                onCardClick = onCardClick,
                onCashClick = onCashClick,
                onPointClick = onPointClick,
                onAddClick = onAddClick,
                onCancelClick = onCancelClick
            )

            MenuList(
                menuList = menuList,
                onAddClick = onMenuAddClick,
                onCancelClick = onMenuCancelClick
            )
        }

    }

    @Composable
    fun TableLayout(
        tableList: List<UiTable>,
        onTableClick: () -> Unit,
        onMergeClick: () -> Unit
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Fixed(10)) {
                items(tableList) {
                    Table(it, onTableClick)
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
                Text(text = "Cash")
            }
            Button(onClick = onPointClick) {
                Text(text = "Cash")
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
}