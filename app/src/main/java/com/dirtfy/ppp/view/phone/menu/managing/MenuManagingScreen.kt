package com.dirtfy.ppp.view.phone.menu.managing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.menu.managing.MenuManagingScreenContract
import com.dirtfy.ppp.contract.viewmodel.MenuManagingContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuManagingScreen : MenuManagingScreenContract.API {
    @Composable
    override fun MenuList(
        menuList: List<MenuManagingContract.DTO.Menu>,
        user: User,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(menuList) {
                MenuItem(
                    menu = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun MenuItem(
        menu: MenuManagingContract.DTO.Menu,
        user: User,
        modifier: Modifier
    ) {
        ListItem(
            headlineContent = { Text(text = menu.name) },
            supportingContent = { Text(text = menu.price) },
            modifier = modifier
        )
    }

    @Composable
    override fun MenuAdd(
        menu: MenuManagingContract.DTO.Menu,
        user: User,
        modifier: Modifier
    ) {
        ElevatedCard(
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = menu.name,
                        onValueChange = { /*TODO*/ }
                    )

                    Spacer(Modifier.size(10.dp))

                    TextField(
                        value = menu.price,
                        onValueChange = { /*TODO*/ }
                    )
                }

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

    @Composable
    override fun Main(
        viewModel: MenuManagingContract.API,
        user: User,
        modifier: Modifier
    ) {
        val newMenu by viewModel.newMenu.collectAsStateWithLifecycle()
        val menuList by viewModel.menuList.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
        ) {
            MenuAdd(
                menu = newMenu,
                user = user,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            MenuList(
                menuList = menuList,
                user = user,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuManagingScreenPreview() {
    val menuList = MutableList(10) {
        MenuManagingContract.DTO.Menu(
            name = "test_${it}",
            price = "23,000"
        )
    }
    val newMenu = MenuManagingContract.DTO.Menu(
        name = "new_test",
        price = "23,000"
    )

    PPPTheme {
        Column(
            modifier = Modifier
        ) {
            MenuManagingScreen.MenuAdd(
                menu = newMenu,
                user = DummyUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            MenuManagingScreen.MenuList(
                menuList = menuList,
                user = DummyUser,
                modifier = Modifier
            )
        }
    }
}