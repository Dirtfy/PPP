package com.dirtfy.ppp.view.phone.selling.tabling

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.common.DummyTablingViewModel
import com.dirtfy.ppp.contract.view.selling.tabling.TableViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TableScreen: TableViewContract.API {
    @Composable
    override fun Table(
        table: TablingViewModelContract.DTO.Table,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .size(35.dp)
                .background(Color(table.color))
                .clickable {
                    if (viewModel.selectedTableNumber.value != 0) {
                        viewModel.deselectTable(viewModel.selectedTableNumber.value)
                    }
                    viewModel.selectTable(table.number.toInt())
                }
        ) {
            Text(text = table.number)
        }
    }

    @Composable
    override fun TableLayout(
        tableList: List<TablingViewModelContract.DTO.Table>,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(10),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
            ) {
                items(tableList) {
                    when(it.number.toInt()) {
                        0 -> {
                            Table(
                                table = TablingViewModelContract.DTO.Table(
                                    number = "",
                                    color = Color.Transparent.value
                                ),
                                viewModel = viewModel,
                                modifier = Modifier
                            )
                        }
                        else -> {
                            Table(
                                table = it,
                                viewModel = viewModel,
                                modifier = Modifier
                            )
                        }
                    }

                }
            }

            Row {
                var base by remember { mutableStateOf("") }
                var merged by remember { mutableStateOf("") }

                Column {
                    Button(onClick = {
                        viewModel.mergeTable(base.toInt(), merged.toInt())
                    }) {
                        Text(text = "merge")
                    }
                    Button(onClick = {
                        viewModel.cleanTable(base.toInt())
                    }) {
                        Text(text = "clean")
                    }
                }

                Column {
                    TextField(
                        label = { Text(text = "base")},
                        value = base, onValueChange = {base = it}
                    )
                    TextField(
                        label = { Text(text = "merged")},
                        value = merged, onValueChange = {merged = it}
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun TableScreenPreview() {
    val tableList: List<TablingViewModelContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TablingViewModelContract.DTO.Table("$it", Color.LightGray.value)
        }

    PPPTheme {
        TableScreen.TableLayout(
            tableList = tableList,
            viewModel = DummyTablingViewModel,
            modifier = Modifier
        )
    }
}
