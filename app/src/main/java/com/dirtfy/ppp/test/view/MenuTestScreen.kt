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
import com.dirtfy.ppp.contract.model.selling.MenuModelContract.DTO.Menu
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.dirtfy.ppp.viewmodel.selling.menu.managing.MenuListViewModel

object MenuTestScreen {
    const val TAG = "AccountTestScreen"
}

@Composable
fun MenuCreate(
    name: String, onNameChange: (String) -> Unit,
    price: String, onPriceChange: (String) -> Unit,
    onCreateButtonClick: () -> Unit
) {
    Column {
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("menu name") }
        )
        TextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("menu price") }
        )
        Button(onClick = { onCreateButtonClick() }) {
            Text(text = "create")
        }
    }

}

@Composable
fun MenuList(menu: List<Menu>) {
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
fun MenuItem(data: Menu) {
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

        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }

        MenuCreate(
            name = name, onNameChange = { name = it },
            price = price, onPriceChange = { price = it }) {

            menuListViewModel.insertData(
                Menu(
                    menuID = null,
                    name = name,
                    price = price.toInt()
                )
            )
        }

        Row {
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