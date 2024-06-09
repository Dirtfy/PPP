package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.tabling.MenuListScreenContract
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuListScreen: MenuListScreenContract.API {

    @Composable
    override fun MenuList(
        menuList: List<TablingContract.DTO.Menu>,
        user: User,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = modifier
        ) {
            items(menuList) {
                this@MenuListScreen.MenuItem(
                    menu = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun MenuItem(
        menu: TablingContract.DTO.Menu,
        user: User,
        modifier: Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = menu.name)

            Row {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    val addIcon = PPPIcons.Add
                    Icon(
                        imageVector = addIcon,
                        contentDescription = addIcon.name
                    )
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    val deleteIcon = PPPIcons.Close
                    Icon(
                        imageVector = deleteIcon,
                        contentDescription = deleteIcon.name
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun MenuListPreview() {
    val menuDataList = MutableList(10) {
        TablingContract.DTO.Menu(
            "test_${it}",
            "${it*1500}"
        )
    }

    PPPTheme {
        MenuListScreen.MenuList(
            menuList = menuDataList,
            user = DummyUser,
            modifier = Modifier
        )
    }
}