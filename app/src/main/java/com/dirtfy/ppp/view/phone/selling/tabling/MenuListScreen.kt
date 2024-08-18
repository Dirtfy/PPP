package com.dirtfy.ppp.view.phone.selling.tabling

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
import com.dirtfy.ppp.common.DummyTablingViewModel
import com.dirtfy.ppp.contract.view.selling.tabling.MenuListViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object MenuListScreen: MenuListViewContract.API {

    @Composable
    override fun MenuList(
        menuList: List<TablingViewModelContract.DTO.Menu>,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
        ) {
            items(menuList) {
                this@MenuListScreen.MenuItem(
                    menu = it,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun MenuItem(
        menu: TablingViewModelContract.DTO.Menu,
        viewModel: TablingViewModelContract.API,
        modifier: Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = menu.name)

            Row {
                IconButton(
                    onClick = {
                        viewModel.orderMenu(menu)
                    }
                ) {
                    val addIcon = PPPIcons.Add
                    Icon(
                        imageVector = addIcon,
                        contentDescription = addIcon.name
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.cancelMenu(menu)
                    }
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

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun MenuListPreview() {
    val menuDataList = MutableList(10) {
        TablingViewModelContract.DTO.Menu(
            "test_${it}",
            "${it*1500}"
        )
    }

    PPPTheme {
        MenuListScreen.MenuList(
            menuList = menuDataList,
            viewModel = DummyTablingViewModel,
            modifier = Modifier
        )
    }
}