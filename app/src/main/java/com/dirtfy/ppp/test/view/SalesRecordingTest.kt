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
import com.dirtfy.ppp.selling.menuManaging.model.MenuData
import com.dirtfy.ppp.selling.salesRecording.model.SalesData
import com.dirtfy.ppp.selling.salesRecording.viewmodel.SalesViewModel

@Composable
fun SalesItem(data: SalesData) {
    LazyRow {
        itemsIndexed(data.menuCountMap.keys.toList()) {_, item ->
            Column {
                MenuItem(data = MenuData(
                    menuID = item,
                    name = data.menuCountMap[item]!!.toString(),
                    price = data.menuPriceMap[item]!!
                ))
            }
        }
    }
}

@Composable
fun SalesCreate(data: SalesData, onCreateButtonClick: () -> Unit) {
    Column {
        SalesItem(data = data)
        Button(onClick = { onCreateButtonClick() }) {
            Text(text = "Create")
        }
    }
}

@Composable
fun SalesList(dataList: List<SalesData>) {
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
        mutableStateOf(SalesData(
            null,
            mapOf("..." to 1),
            mapOf("..." to 999),
            null)
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