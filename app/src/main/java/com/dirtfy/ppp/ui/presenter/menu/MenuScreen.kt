package com.dirtfy.ppp.ui.presenter.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.holder.MenuViewModel
import com.dirtfy.ppp.ui.holder.menu.HolderMenu
import com.dirtfy.ppp.ui.holder.menu.MenuHolder

object MenuScreen {

    @Composable
    fun Main(
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {
        LaunchedEffect(key1 = holder) {
            holder.updateMenuList()
        }

        Column {
            NewMenu(holder)
            _MenuList(holder)
        }
    }

    @Composable
    fun NewMenu(
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {
        val newMenu by holder.newMenu.collectAsStateWithLifecycle()

        Row {
            Column {
                TextField(
                    value = newMenu.name,
                    onValueChange = {
                        holder.request {
                            updateNewMenu(newMenu.copy(name = it))
                        }
                    }
                )
                TextField(
                    value = newMenu.price,
                    onValueChange = {
                        holder.request {
                            updateNewMenu(newMenu.copy(price = it))
                        }
                    }
                )
            }

            Button(onClick = { holder.request { createMenu(newMenu) } }) {
                Text(text = "create")
            }
        }
    }

    @Composable
    fun _MenuList(
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {
        val menuListState by holder.menuList.collectAsStateWithLifecycle()

        when(menuListState) {
            is FlowState.Loading -> {
                MenuListLoading()
            }
            is FlowState.Success -> {
                val menuList = (menuListState as FlowState.Success<List<HolderMenu>>).data
                MenuList(menuList = menuList, holder = holder)
            }
            is FlowState.Failed -> {
                val throwable = (menuListState as FlowState.Failed<List<HolderMenu>>).throwable
                MenuListFailure(throwable = throwable)
            }
        }
    }

    @Composable
    fun MenuListLoading() {
        Surface {
            Text(text = "loading..")
        }
    }

    @Composable
    fun MenuList(
        menuList: List<HolderMenu>,
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {
        LazyColumn {
            items(menuList) {
                MenuListItem(menu = it, holder = holder)
            }
        }
    }

    @Composable
    fun MenuListFailure(
        throwable: Throwable,
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {

    }

    @Composable
    fun MenuListItem(
        menu: HolderMenu,
        holder: MenuHolder = viewModel<MenuViewModel>()
    ) {
        Row {
            Text(text = menu.name)
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = menu.price)

            Button(onClick = { holder.request { deleteMenu(menu) } }) {
                Text(text = "delete")
            }
        }
    }


}