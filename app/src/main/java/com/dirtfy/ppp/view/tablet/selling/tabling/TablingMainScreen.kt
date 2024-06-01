package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.contract.DummyUser
import com.dirtfy.ppp.contract.User
import com.dirtfy.ppp.contract.view.tabling.MenuListScreenContract
import com.dirtfy.ppp.contract.view.tabling.OrderScreenContract
import com.dirtfy.ppp.contract.view.tabling.TableScreenContract
import com.dirtfy.ppp.contract.view.tabling.TablingScreenContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TablingMainScreen: TablingScreenContract.API {
    @Composable
    override fun Main(
        orderList: List<OrderScreenContract.DTO.Order>,
        totalOrderData: OrderScreenContract.DTO.Total,
        tableList: List<TableScreenContract.DTO.Table>,
        menuList: List<MenuListScreenContract.DTO.Menu>,
        user: User,
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            OrderScreen.Main(
                user = user,
                orderList = orderList,
                total = totalOrderData
            )
            Column {
                TableScreen.TableLayout(
                    tableList = tableList,
                    user = user,
                    modifier = Modifier
                )
                MenuListScreen.MenuList(
                    menuList = menuList,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun InstantMenuCreation(user: User, modifier: Modifier) {
        TODO("Not yet implemented")
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun TablingMainScreenPreview() {
    val orderList = MutableList(10) {
        OrderScreenContract.DTO.Order(
            "test_${it}",
            "${it*1234}",
            "$it"
        )
    }
    val totalPrice = orderList.sumOf { it.price.toInt() }
    val total =
        OrderScreenContract.DTO.Total("$totalPrice")

    val tableList: List<TableScreenContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TableScreenContract.DTO.Table("$it", Color.LightGray.value)
        }

    val menuDataList = MutableList(10) {
        MenuListScreenContract.DTO.Menu(
            "test_${it}",
            "${it*1500}"
        )
    }

    PPPTheme {
        TablingMainScreen.Main(
            orderList = orderList,
            totalOrderData = total,
            tableList = tableList,
            menuList = menuDataList,
            user = DummyUser,
            modifier = Modifier
        )
    }
}