package com.dirtfy.ppp.view.phone.selling.tabling

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.common.DummyTablingViewModel
import com.dirtfy.ppp.contract.view.selling.tabling.TablingViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TablingMainScreen: TablingViewContract.API {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Main(
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        val orderList by viewModel.orderList
        val total by viewModel.total
        val tableList by viewModel.tableList
        val menuList by viewModel.menuList

        LaunchedEffect(key1 = viewModel) {
            viewModel.checkMenu()
            viewModel.checkTables()
            viewModel.checkOrder()
        }

        Column(
            modifier = modifier
        ) {
            TableScreen.TableLayout(
                tableList = tableList,
                viewModel = viewModel,
                modifier = Modifier.wrapContentHeight()
            )

            HorizontalPager(
                state = rememberPagerState{ 2 }
            ) { page ->
                when(page) {
                    1 ->  {
                        viewModel.checkOrder()
                        OrderScreen.Main(
                            viewModel = viewModel,
                            orderList = orderList,
                            total = total,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                    }
                    else -> {
                        MenuListScreen.MenuList(
                            menuList = menuList,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun InstantMenuCreation(
        menu: TablingViewModelContract.DTO.Menu,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(
                modifier = modifier
            ) {
                Column {
                    TextField(
                        value = menu.name,
                        onValueChange = {

                        }
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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun TablingMainScreenPreview() {
    val orderList = MutableList(10) {
        TablingViewModelContract.DTO.Order(
            "test_${it}",
            "${it*1234}",
            "$it"
        )
    }
    val totalPrice = orderList.sumOf { it.price.toInt() }
    val total =
        TablingViewModelContract.DTO.Total("$totalPrice")

    val tableList: List<TablingViewModelContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TablingViewModelContract.DTO.Table("$it", Color.LightGray.value)
        }

    val menuDataList = MutableList(10) {
        TablingViewModelContract.DTO.Menu(
            "test_${it}",
            "${it*1500}"
        )
    }

    PPPTheme {
        Column {
            TableScreen.TableLayout(
                tableList = tableList,
                viewModel = DummyTablingViewModel,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            HorizontalPager(
                state = rememberPagerState{ 2 }
            ) { page ->
                when(page) {
                    1 ->  {
                        OrderScreen.Main(
                            viewModel = DummyTablingViewModel,
                            orderList = orderList,
                            total = total,
                            modifier = Modifier
                                .fillMaxHeight()
                                .widthIn(200.dp, 300.dp)
                        )
                    }
                    else -> {
                        MenuListScreen.MenuList(
                            menuList = menuDataList,
                            viewModel = DummyTablingViewModel,
                            modifier = Modifier.widthIn(600.dp, 800.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun InstantMenuCreationPreview(){
    val menu = TablingViewModelContract.DTO.Menu(
        name = "test",
        price = "123,000"
    )
    PPPTheme {
        TablingMainScreen.InstantMenuCreation(
            menu = menu,
            viewModel = DummyTablingViewModel,
            modifier = Modifier
        )
    }
}