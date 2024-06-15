package com.dirtfy.ppp.view.tablet.selling.menu.managing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.view.menu.managing.MenuManagingScreenContract
import com.dirtfy.ppp.contract.viewmodel.MenuManagingContract
import com.dirtfy.ppp.contract.viewmodel.user.DummyUser
import com.dirtfy.ppp.contract.viewmodel.user.User
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuManagingScreen : MenuManagingScreenContract.API {
    @Composable
    override fun MenuList(
        menuList: List<MenuManagingContract.DTO.Menu>,
        user: User,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
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

                Spacer(Modifier.size(10.dp))

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
        val menuList by viewModel.menuList.collectAsStateWithLifecycle()
        val newMenu by viewModel.newMenu.collectAsStateWithLifecycle()

        Row(
            modifier = modifier
        ) {
            MenuList(
                menuList = menuList,
                user = user,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            MenuAdd(
                menu = newMenu,
                user = user,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
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
        Row(
            modifier = Modifier
        ) {
            MenuManagingScreen.MenuList(
                menuList = menuList,
                user = DummyUser,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            MenuManagingScreen.MenuAdd(
                menu = newMenu,
                user = DummyUser,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )
        }
    }
}