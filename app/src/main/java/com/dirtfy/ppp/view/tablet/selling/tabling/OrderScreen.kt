package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.dirtfy.ppp.viewmodel.selling.tabling.TableOrderData

@Composable
fun OrderItem(
    menuName: String,
    count: Int,
    price: Int
) {
    Row {
        Text(text = menuName)
        Text(text = "$count")
        Text(text = "$price")
    }
}

@Composable
fun NameAndValueRow(
    name: String,
    value: String
) {
    Row {
        Text(text = name)
        Text(text = value)
    }
}

@Composable
fun OrderScreen(
    orderList: TableOrderData
) {
    Column {
        LazyColumn {
            items(orderList.countMap.keys.toList()) {
                OrderItem(
                    menuName = it,
                    count = orderList.countMap[it]!!,
                    price = orderList.priceMap[it]!!
                )
            }
        }

        NameAndValueRow(name = "total", value = "49845")
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    val testOrderList  =
        TableOrderData(
            mapOf(
                "test" to 1,
                "test1" to 2
            ),
            mapOf(
                "test" to 1324,
                "test1" to 1298
            )
        )

    PPPTheme {
        OrderScreen(testOrderList)
    }
}