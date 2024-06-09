package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.tabling.TablingScreenContract
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TablingMainScreen: TablingScreenContract.API {

    @Composable
    override fun Main(
        viewModel: TablingContract.API,
        modifier: Modifier,
        user: User
    ) {
        val orderList by viewModel.orderList.collectAsStateWithLifecycle()
        val total by viewModel.total.collectAsStateWithLifecycle()
        val tableList by viewModel.tableList.collectAsStateWithLifecycle()
        val menuList by viewModel.menuList.collectAsStateWithLifecycle()

        Row(
            modifier = modifier
        ) {
            OrderScreen.Main(
                user = user,
                orderList = orderList,
                total = total,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(200.dp, 300.dp)
            )
            Column {
                TableScreen.TableLayout(
                    tableList = tableList,
                    user = user,
                    modifier = Modifier.widthIn(600.dp, 800.dp)
                )
                MenuListScreen.MenuList(
                    menuList = menuList,
                    user = user,
                    modifier = Modifier.widthIn(600.dp, 800.dp)
                )
            }
        }
    }

    @Composable
    override fun InstantMenuCreation(
        menu: TablingContract.DTO.Menu,
        user: User,
        modifier: Modifier
    ) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(
                modifier = modifier
            ) {
                Column {
                    TextField(
                        value = menu.name,
                        onValueChange = { /*TODO*/ }
                    )
                    TextField(
                        value = menu.price,
                        onValueChange = { /*TODO*/ }
                    )

                    IconButton(onClick = { /*TODO*/ }) {
                        val addIcon = PPPIcons.Add
                        Icon(
                            imageVector = addIcon,
                            contentDescription = addIcon.name
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun TablingMainScreenPreview() {
    val orderList = MutableList(10) {
        TablingContract.DTO.Order(
            "test_${it}",
            "${it*1234}",
            "$it"
        )
    }
    val totalPrice = orderList.sumOf { it.price.toInt() }
    val total =
        TablingContract.DTO.Total("$totalPrice")

    val tableList: List<TablingContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TablingContract.DTO.Table("$it", Color.LightGray.value)
        }

    val menuDataList = MutableList(10) {
        TablingContract.DTO.Menu(
            "test_${it}",
            "${it*1500}"
        )
    }

    PPPTheme {
        Row(
            modifier = Modifier
        ) {
            OrderScreen.Main(
                user = DummyUser,
                orderList = orderList,
                total = total,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(200.dp, 300.dp)
            )
            Column {
                TableScreen.TableLayout(
                    tableList = tableList,
                    user = DummyUser,
                    modifier = Modifier.widthIn(600.dp, 800.dp)
                )
                MenuListScreen.MenuList(
                    menuList = menuDataList,
                    user = DummyUser,
                    modifier = Modifier.widthIn(600.dp, 800.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun InstantMenuCreationPreview(){
    val menu = TablingContract.DTO.Menu(
        name = "test",
        price = "123,000"
    )
    PPPTheme {
        TablingMainScreen.InstantMenuCreation(
            menu = menu,
            user = DummyUser,
            modifier = Modifier
        )
    }
}