package com.dirtfy.ppp.ui.presenter.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.holder.menu.HolderMenu
import com.dirtfy.ppp.ui.holder.menu.MenuHolder
import com.dirtfy.tagger.Tagger

object MenuTestScreen: Tagger {

    @Composable
    fun Main(
        holder: MenuHolder
    ) {
        val menuListState by holder.menuList.collectAsStateWithLifecycle()
        val newMenu by holder.newMenu.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = holder) {
            holder.request { updateMenuList() }
        }

        Column {
            NewMenu(
                newMenu = newMenu,
                onMenuChanged = { holder.request { updateNewMenu(it) } },
                onMenuAdd = { holder.request { createMenu(it) } }
            )

            when(menuListState) {
                is FlowState.Success -> {
                    val menuList = (menuListState as FlowState.Success<List<HolderMenu>>).data
                    LazyColumn {
                        items(menuList) {
                            Row {
                                Text(text = it.name)
                                Text(text = it.price)
                                Button(onClick = { holder.request { deleteMenu(it) } }) {
                                    Text(text = "delete")
                                }
                            }
                        }
                    }
                }
                is FlowState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "loading...")
                    }
                }
                is FlowState.Failed -> {
                    val throwable = (menuListState as FlowState.Failed<List<HolderMenu>>).throwable

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = { holder.request { updateMenuList() } }) {
                                Text(text = "OK")
                            }
                        },
                        title = { Text(text = throwable.message?: "unknown error") }
                    )

                }
            }
        }

    }

    @Composable
    fun NewMenu(
        newMenu: HolderMenu,
        onMenuChanged: (HolderMenu) -> Unit,
        onMenuAdd: (HolderMenu) -> Unit
    ) {
        Row {
            Column {
                TextField(
                    value = newMenu.name,
                    onValueChange = {
                        onMenuChanged(newMenu.copy(name = it))
                    }
                )
                TextField(
                    value = newMenu.price,
                    onValueChange = {
                        onMenuChanged(newMenu.copy(price = it))
                    }
                )
            }
            Button(onClick = { onMenuAdd(newMenu) }) {
                Text(text = "add")
            }
        }
    }
}