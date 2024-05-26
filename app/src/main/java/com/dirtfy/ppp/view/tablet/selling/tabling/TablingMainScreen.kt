package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.dirtfy.ppp.viewmodel.selling.tabling.TableViewModel

@Composable
fun TablingMainScreen(
    tableViewModel: TableViewModel = viewModel()
) {
    val tableList by tableViewModel.tableList.collectAsStateWithLifecycle()
    val selectedTableOrders by tableViewModel.selectedTable.collectAsStateWithLifecycle()
    val testTableList by remember {
        mutableStateOf(
            listOf(
                11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 1
            )
        )
    }
    val menuList by remember {
        mutableStateOf(
            listOf(
                1, 2, 3, 4, 5, 6
            )
        )
    }

    Row {
        OrderScreen(selectedTableOrders)
        Column {
            TableScreen(testTableList)
            MenuListScreen(menuList)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TablingMainScreenPreview() {
    PPPTheme {
        TablingMainScreen()
    }
}