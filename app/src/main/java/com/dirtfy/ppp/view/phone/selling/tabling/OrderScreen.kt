package com.dirtfy.ppp.view.phone.selling.tabling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dirtfy.ppp.contract.view.tabling.OrderScreenContract
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.contract.viewmodel.user.DummyUser
import com.dirtfy.ppp.contract.viewmodel.user.User
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object OrderScreen: OrderScreenContract.API {

    private val listWidthRequest: Modifier = Modifier.widthIn(100.dp, 500.dp)

    @Composable
    override fun OrderList(
        orderList: List<TablingContract.DTO.Order>,
        user: User,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(orderList) {
                OrderItem(
                    order = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun OrderItem(
        order: TablingContract.DTO.Order,
        user: User,
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
        total: TablingContract.DTO.Total,
        user: User,
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
        user: User,
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "pay")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "pay")
            }
        }
    }
    
    @Composable
    fun Main(
        user: User,
        orderList: List<TablingContract.DTO.Order>,
        total: TablingContract.DTO.Total,
        modifier: Modifier = Modifier
    ) {
        ConstraintLayout(
            modifier = modifier
        ) {
            val (orderLayout, totalLayout, buttonLayout) = createRefs()
            
            OrderList(
                orderList = orderList,
                user = user,
                modifier = Modifier.constrainAs(orderLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(totalLayout.top)
                    height = Dimension.fillToConstraints
                }
            )

            Total(
                total = total,
                user = user,
                modifier = Modifier.constrainAs(totalLayout) {
                    bottom.linkTo(buttonLayout.top)
                }
            )

            PaymentSelector(
                user = user,
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
        TablingContract.DTO.Order(
            "test_${it}",
            "${it*1234}",
            "$it"
        )
    }
    val totalPrice = orderList.sumOf { it.price.toInt() }
    val total =
        TablingContract.DTO.Total("$totalPrice")

    PPPTheme {
        OrderScreen.Main(
            user = DummyUser,
            orderList = orderList,
            total = total,
            modifier = Modifier.fillMaxHeight()
        )
    }
}