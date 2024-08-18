package com.dirtfy.ppp.view.phone.selling.tabling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dirtfy.ppp.common.DummyTablingViewModel
import com.dirtfy.ppp.contract.view.selling.tabling.OrderViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object OrderScreen: OrderViewContract.API {

    private val listWidthRequest: Modifier = Modifier.widthIn(100.dp, 500.dp)

    @Composable
    override fun OrderList(
        orderList: List<TablingViewModelContract.DTO.Order>,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(orderList) {
                OrderItem(
                    order = it,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun OrderItem(
        order: TablingViewModelContract.DTO.Order,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        ListItem(
            leadingContent = {
                Text(text = order.name)
            },
            headlineContent = {
                Text(text = order.count)
            },
            trailingContent = {
                Text(text = order.price)
            },
            modifier = listWidthRequest
        )
    }

    @Composable
    override fun Total(
        total: TablingViewModelContract.DTO.Total,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Box(modifier = modifier) {
            ListItem(
                leadingContent = {
                    Text(text = "total")
                },
                headlineContent = {

                },
                trailingContent = {
                    Text(text = total.price)
                },
                modifier = listWidthRequest
            )
        }
        
    }

    @Composable
    override fun PaymentSelector(
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            Column {
                var pointAccountNumber by remember{ mutableStateOf("") }
                Button(onClick = {
                    viewModel.payTable(
                        TablingViewModelContract.DTO.Payment.Point,
                        pointAccountNumber
                    )
                }) {
                    Text(text = "Point")
                }
                TextField(
                    value = pointAccountNumber,
                    onValueChange = {
                        pointAccountNumber = it
                    })
            }

            Button(onClick = {
                viewModel.payTable(
                    TablingViewModelContract.DTO.Payment.Card,
                    "Card"
                )
            }) {
                Text(text = "card")
            }

            Button(onClick = {
                viewModel.payTable(
                    TablingViewModelContract.DTO.Payment.Cash,
                    "Cash"
                )
            }) {
                Text(text = "cash")
            }
        }
    }
    
    @Composable
    fun Main(
        viewModel: TablingViewModelContract.API,
        orderList: List<TablingViewModelContract.DTO.Order>,
        total: TablingViewModelContract.DTO.Total,
        modifier: Modifier = Modifier
    ) {
        ConstraintLayout(
            modifier = modifier
        ) {
            val (orderLayout, totalLayout, buttonLayout) = createRefs()
            
            OrderList(
                orderList = orderList,
                viewModel = viewModel,
                modifier = Modifier.constrainAs(orderLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(totalLayout.top)
                    height = Dimension.fillToConstraints
                }
            )

            Total(
                total = total,
                viewModel = viewModel,
                modifier = Modifier.constrainAs(totalLayout) {
                    bottom.linkTo(buttonLayout.top)
                }
            )

            PaymentSelector(
                viewModel = viewModel,
                modifier = Modifier.constrainAs(buttonLayout) {
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun OrderScreenPreview() {
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

    PPPTheme {
        OrderScreen.Main(
            viewModel = DummyTablingViewModel,
            orderList = orderList,
            total = total,
            modifier = Modifier.fillMaxHeight()
        )
    }
}