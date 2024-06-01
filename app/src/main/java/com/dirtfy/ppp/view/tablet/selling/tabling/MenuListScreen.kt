package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.contract.DummyUser
import com.dirtfy.ppp.contract.User
import com.dirtfy.ppp.contract.view.tabling.MenuListScreenContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuListScreen: MenuListScreenContract.API {

    @Composable
    override fun MenuList(
        menuList: List<MenuListScreenContract.DTO.Menu>,
        user: User,
        modifier: Modifier
    ) {
        LazyColumn {
            items(menuList) {
                this@MenuListScreen.MenuItem(
                    menu = it,
                    user = user,
                    modifier = modifier
                )
            }
        }
    }

    @Composable
    override fun MenuItem(
        menu: MenuListScreenContract.DTO.Menu,
        user: User,
        modifier: Modifier
    ) {
        ListItem(
            headlineContent = { Text(text = menu.name) },
            supportingContent = { Text(text = menu.price) },
            trailingContent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuListPreview() {
    val menuDataList = MutableList(10) {
        MenuListScreenContract.DTO.Menu(
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