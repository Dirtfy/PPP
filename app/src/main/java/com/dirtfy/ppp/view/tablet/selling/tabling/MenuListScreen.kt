package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.view.ui.theme.PPPTheme

@Composable
fun MenuItem(
    menu: Int
) {
    Text(text = "$menu")
}

@Composable
fun MenuListScreen(
    menuList: List<Int>
) {
    LazyColumn {
        items(menuList) {
            MenuItem(menu = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuListScreenPreview() {
    val testMenuList  = listOf(
        1, 2, 3, 4, 5, 6
    )

    PPPTheme {
        MenuListScreen(menuList = testMenuList)
    }
}