package com.dirtfy.ppp.view.phone.selling.menu.managing

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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.common.DummyMenuManagingViewModel
import com.dirtfy.ppp.contract.view.selling.menu.managing.MenuManagingViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.menu.managing.MenuManagingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuManagingScreen : MenuManagingViewContract.API {
    @Composable
    override fun MenuList(
        menuList: List<MenuManagingViewModelContract.DTO.Menu>,
        viewModel: MenuManagingViewModelContract.MenuList.API,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(menuList) {
                MenuItem(
                    menu = it,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun MenuItem(
        menu: MenuManagingViewModelContract.DTO.Menu,
        viewModel: MenuManagingViewModelContract.MenuList.API,
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            ListItem(
                headlineContent = { Text(text = menu.name) },
                supportingContent = { Text(text = menu.price) },
                trailingContent = {
                    IconButton(onClick = {
                        viewModel.deleteMenu(menu)
                    }) {
                        val deleteIcon = PPPIcons.Close
                        Icon(
                            imageVector = deleteIcon,
                            contentDescription = deleteIcon.name
                        )
                    }
                }
            )
        }

    }

    @Composable
    override fun NewMenu(
        menu: MenuManagingViewModelContract.DTO.Menu,
        viewModel: MenuManagingViewModelContract.NewMenu.API,
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
                        label = { Text(text = "menu name")},
                        value = menu.name,
                        onValueChange = {
                            viewModel.setNewMenu(
                                menu.copy(name = it)
                            )
                        }
                    )

                    Spacer(Modifier.size(10.dp))

                    TextField(
                        label = { Text(text = "menu price")},
                        value = menu.price,
                        onValueChange = {
                            viewModel.setNewMenu(
                                menu.copy(price = it)
                            )
                        }
                    )
                }

                IconButton(onClick = {
                    viewModel.addMenu()
                }) {
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
        viewModel: MenuManagingViewModelContract.API,
        modifier: Modifier
    ) {
        val newMenu by viewModel.newMenu
        val menuList by viewModel.menuList
        
        LaunchedEffect(key1 = viewModel) {
            viewModel.checkMenuList()
        }

        Column(
            modifier = modifier
        ) {
            NewMenu(
                menu = newMenu,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            MenuList(
                menuList = menuList,
                viewModel = viewModel,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun MenuManagingScreenPreview() {
    val menuList = MutableList(10) {
        MenuManagingViewModelContract.DTO.Menu(
            name = "test_${it}",
            price = "23,000"
        )
    }
    val newMenu = MenuManagingViewModelContract.DTO.Menu(
        name = "new_test",
        price = "23,000"
    )

    PPPTheme {
        Column(
            modifier = Modifier
        ) {
            MenuManagingScreen.NewMenu(
                menu = newMenu,
                viewModel = DummyMenuManagingViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            MenuManagingScreen.MenuList(
                menuList = menuList,
                viewModel = DummyMenuManagingViewModel,
                modifier = Modifier
            )
        }
    }
}