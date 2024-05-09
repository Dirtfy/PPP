package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.ordering.menuManaging.model.MenuData
import com.dirtfy.ppp.ordering.menuManaging.viewmodel.MenuListViewModel
import com.dirtfy.ppp.ui.theme.PPPTheme

object MenuTestScreen {
    const val TAG = "AccountTestScreen"
}

@Composable
fun MenuList(menu: List<MenuData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = menu,
            key = { item ->
                item.menuID?:""
            }
        ) { item ->
            MenuItem(data = item)
        }
    }
}

@Composable
fun MenuItem(data: MenuData) {
    Column{
        Text(text = "ID: ${data.menuID?:"null"}")
        Text(text = "Name: "+ data.name)
        Text(text = "Price:" + data.price.toString())
    }
}

@Composable
fun MenuTest(
    menuListViewModel: MenuListViewModel = viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        var nameText by remember { mutableStateOf("") }
        TextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("menu name") }
        )

        var priceText by remember { mutableStateOf("") }
        TextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("menu price") }
        )

        Row {
            Button(onClick = {
                menuListViewModel.insertData(
                    MenuData(
                        menuID = null,
                        name = nameText,
                        price = priceText.toInt()
                    )
                )
                nameText = ""
                priceText = ""
            }) {
                Text(text = "create")
            }
            Button(onClick = {
                menuListViewModel.reloadData { true }
            }) {
                Text(text = "readAll")
            }
            Button(onClick = { menuListViewModel.clearData() }) {
                Text(text = "clear")
            }
        }

        val menuData by menuListViewModel.dataList.collectAsStateWithLifecycle(listOf())
        MenuList(menu = menuData)
    }
}

@Preview(showBackground = true)
@Composable
fun MenuTestPreview() {
    PPPTheme {
        MenuTest()
    }
}