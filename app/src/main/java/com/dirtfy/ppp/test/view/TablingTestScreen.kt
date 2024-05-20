package com.dirtfy.ppp.test.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.selling.tabling.viewmodel.TableOrderData
import com.dirtfy.ppp.selling.tabling.viewmodel.TableViewModel

@Composable
fun Table(
    tableNumber: Int,
    tableColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(PaddingValues(Dp(2F)))
            .clickable {
                onClick()
            }
    ) {
        Text(text = "$tableNumber", modifier = Modifier.background(tableColor))
    }
}

@Composable
fun MergeFrame(
    onMergeStartButtonClick: (List<Int>) -> Unit
) {
    var list by remember { mutableStateOf("") }

    Column {
        TextField(value = list, onValueChange = { list = it })
        Button(onClick = {
            onMergeStartButtonClick(
                list.split(',')
                    .map { it.toInt() }
            )
        }) {
            Text(text = "merge start")
        }
    }
}

@Composable
fun TableInfo(
    tableData: TableOrderData,
    onMenuAdd: (name: String, price: String) -> Unit,
    onCleanClick: () -> Unit
) {
    var menuName by remember { mutableStateOf("") }
    var menuPrice by remember { mutableStateOf("") }

    Surface {
        Column {
            MenuCreate(
                name = menuName, onNameChange = { menuName = it },
                price = menuPrice, onPriceChange = { menuPrice = it }) {

                onMenuAdd(menuName, menuPrice)
            }

            Button(onClick = onCleanClick) {
                Text(text = "table Clean")
            }

            LazyColumn {
                itemsIndexed(tableData.countMap.keys.toList()) { _, key ->
                    Row {
                        Text(text = key)
                        Text(text = tableData.countMap[key].toString())
                        Text(text = tableData.priceMap[key].toString())
                    }
                }
            }
        }
    }
}

@Composable
fun TablingTestScreen(
    tableViewModel: TableViewModel = viewModel()
) {

    val tableList by tableViewModel.tableList.collectAsStateWithLifecycle()
    var activeTableInfo by remember { mutableStateOf(false) }
    var currentTableNumber by remember { mutableStateOf(0) }

    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(10)
        ) {
            items(tableList.size) {
                Table(it, Color.Gray) {
                    activeTableInfo = !activeTableInfo
                    currentTableNumber = it
                }
            }
        }

        MergeFrame(onMergeStartButtonClick = {
            tableViewModel.groupTable(it)
        })

        if (activeTableInfo) {
            val tableData by tableList[currentTableNumber].collectAsStateWithLifecycle()

            TableInfo(tableData = tableData,
                onMenuAdd = {
                            name, price ->
                    tableViewModel.menuAdd(currentTableNumber, name, price.toInt())
                            },
                onCleanClick = {
                    tableViewModel.cleanTable(currentTableNumber)
                })
        }
    }

}