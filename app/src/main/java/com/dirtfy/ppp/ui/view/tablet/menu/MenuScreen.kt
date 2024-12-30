package com.dirtfy.ppp.ui.view.tablet.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.view.Component
import com.dirtfy.tagger.Tagger
import javax.inject.Inject

class MenuScreen @Inject constructor(
    private val menuController: MenuController
): Tagger {

    @Composable
    fun Main(
        controller: MenuController = menuController
    ) {
        val screen by controller.screenData.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.request { updateMenuList() }
        }

        Component.HandleUiStateDialog(
            uiState = screen.createMenuState,
            onDismissRequest = { controller.setCreateMenuState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { createMenu() } }
        )
        Component.HandleUiStateDialog(
            uiState = screen.deleteMenuState,
            onDismissRequest = { controller.setDeleteMenuState(UiScreenState(UiState.COMPLETE)) }
        )

        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            NewMenu(
                newMenu = screen.newMenu,
                onMenuChanged = { controller.updateNewMenu(it) },
                onMenuAdd = { controller.request { createMenu() } }
            )

            Spacer(modifier = Modifier.size(10.dp))

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Component.SearchBar(
                    searchClue = screen.searchClue, onClueChanged = {
                        controller.request { updateSearchClue(it) }
                    },
                    placeholder = stringResource(R.string.menu_name)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Component.HandleUiStateDialog(
                    uiState = screen.menuListState,
                    onDismissRequest = { controller.setMenuListState(UiScreenState(UiState.COMPLETE)) },
                    onRetryAction = { controller.retryUpdateMenuList() },
                    onComplete = {
                        if (screen.menuList.isEmpty()) {
                            Text(
                                text = stringResource(R.string.empty_list),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(200.dp),
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                items(
                                    items = screen.menuList,
                                    key = { it.name }
                                ) {
                                    Menu(
                                        menu = it,
                                        onDeleteClick = {
                                            controller.request { deleteMenu(it) }
                                        }
                                    )
                                }
                            }
                        }

                    }
                )
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
                            label = { Text(text = stringResource(R.string.menu_name)) },
                            value = newMenu.name,
                            onValueChange = {
                                onMenuChanged(newMenu.copy(name = it))
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        TextField(
                            label = { Text(text = stringResource(R.string.menu_price)) },
                            value = newMenu.price,
                            onValueChange = {
                                onMenuChanged(newMenu.copy(price = it))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = Component.CurrencyInputVisualTransformation()
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(
                            onClick = { onMenuAdd(newMenu) }
                        ) {
                            Text(text = stringResource(R.string.ok))
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun Menu(
        menu: UiMenu,
        onDeleteClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = menu.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = menu.price, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

}