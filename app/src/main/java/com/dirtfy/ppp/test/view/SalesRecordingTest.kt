package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.contract.model.selling.MenuModelContract.DTO.Menu
import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract.DTO.Sales
import com.dirtfy.ppp.viewmodel.selling.recording.SalesViewModel

@Composable
fun SalesItem(data: Sales) {
    LazyRow {
        itemsIndexed(data.menuCountMap.keys.toList()) {_, item ->
            Column {
                MenuItem(data = Menu(
                    menuID = item,
                    name = data.menuCountMap[item]!!.toString(),
                    price = data.menuPriceMap[item]!!
                )
                )
            }
        }
    }
}

@Composable
fun SalesCreate(data: Sales, onCreateButtonClick: () -> Unit) {
    Column {
        SalesItem(data = data)
        Button(onClick = { onCreateButtonClick() }) {
            Text(text = "Create")
        }
    }
}

@Composable
fun SalesList(dataList: List<Sales>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = dataList
        ) { item ->
            SalesItem(data = item)
        }
    }
}

@Composable
fun SalesRecordingTest(
    salesViewModel: SalesViewModel = viewModel()
) {

    var menuName by remember { mutableStateOf("") }
    var menuPrice by remember { mutableStateOf("") }
    var salesData by remember {
        mutableStateOf(
            Sales(
            salesID = null,
            menuCountMap = mapOf("..." to 1),
            menuPriceMap = mapOf("..." to 999),
            pointAccountNumber = null)
        )
    }

    Column {
        MenuCreate(
            name = menuName, onNameChange = { menuName = it },
            price = menuPrice, onPriceChange = { menuPrice = it }
        ) {
            val menuCountMutableMap = salesData.menuCountMap.toMutableMap()
            val menuPriceMutableMap = salesData.menuPriceMap.toMutableMap()
            if (menuCountMutableMap[menuName] == null) {
                menuCountMutableMap[menuName] = 1
            }
            else {
                menuCountMutableMap[menuName] = menuCountMutableMap[menuName]!! + 1
            }

            menuPriceMutableMap[menuName] = menuPrice.toInt()

            salesData = salesData.copy(
                menuCountMap = menuCountMutableMap,
                menuPriceMap = menuPriceMutableMap
            )
        }
        SalesCreate(
            data = salesData,
            onCreateButtonClick = {
                salesViewModel.insertData(salesData)
            }
        )

        val salesList by salesViewModel.dataList.collectAsStateWithLifecycle()
        SalesList(dataList = salesList)
    }
}