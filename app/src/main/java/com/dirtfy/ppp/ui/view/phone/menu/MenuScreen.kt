package com.dirtfy.ppp.ui.view.phone.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import com.dirtfy.ppp.ui.presenter.viewmodel.MenuViewModel
import com.dirtfy.ppp.ui.view.phone.Component
import com.dirtfy.tagger.Tagger
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject


class MenuScreen @Inject constructor(
    val menuController: MenuController
): Tagger {

//    class SecretFriend @Inject constructor(
//        val menuController: MenuController
//    ) {
//
//    }



    @Composable
    fun Main(
        controller: MenuController = menuController
    ) {
        val menuListState by controller.menuList.collectAsStateWithLifecycle()
        val searchClue by controller.searchClue.collectAsStateWithLifecycle()
        val newMenu by controller.newMenu.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.request { updateMenuList() }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Component.SearchBar(
                searchClue = searchClue, onClueChanged = {
                    controller.request { updateSearchClue(it) }
                },
                placeholder = "menu name"
            )
            
            NewMenu(
                newMenu = newMenu,
                onMenuChanged = {controller.request { updateNewMenu(it) }},
                onMenuAdd = { controller.request { createMenu(it) } }
            )

            Spacer(modifier = Modifier.size(10.dp))

            when(menuListState) {
                is FlowState.Success -> {
                    val menuList = (menuListState as FlowState.Success<List<UiMenu>>).data
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(
                            items = menuList,
                            key = { it.name }
                        ) {
                            ListItem(
                                headlineContent = { Text(it.name) },
                                supportingContent = {
                                    Text(it.price)
                                },
                                trailingContent = {
                                    IconButton(
                                        onClick = {
                                            controller.request { deleteMenu(it) }
                                        }
                                    ) {
                                        val icon = Icons.Filled.Close
                                        Icon(imageVector = icon, contentDescription = icon.name)
                                    }
                                }
                            )
                        }
                    }
                }
                is FlowState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is FlowState.Failed -> {
                    val throwable = (menuListState as FlowState.Failed<List<UiMenu>>).throwable

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = { controller.request { updateMenuList() } }) {
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
        newMenu: UiMenu,
        onMenuChanged: (UiMenu) -> Unit,
        onMenuAdd: (UiMenu) -> Unit
    ) {
        Box {
            Card(
                modifier = Modifier.padding(10.dp),
            ) {
                Box(
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        TextField(
                            label = { Text(text = "menu name") },
                            value = newMenu.name,
                            onValueChange = {
                                onMenuChanged(newMenu.copy(name = it))
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        TextField(
                            label = { Text(text = "menu price") },
                            value = newMenu.price,
                            onValueChange = {
                                onMenuChanged(newMenu.copy(price = it))
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(
                            onClick = { onMenuAdd(newMenu) }
                        ) {
                            Text(text = "add")
                        }
                    }
                }

            }
        }
    }


}