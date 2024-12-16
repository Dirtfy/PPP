package com.dirtfy.ppp.ui.view.phone.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiNewMenu
import com.dirtfy.ppp.ui.view.phone.Component
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

        Component.HandleUiStateDialog(
            uiState = screen.createMenuState,
            onDismissRequest = { controller.setCreateMenuState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = { controller.request { createMenu() } }
        )
        Component.HandleUiStateDialog(
            uiState = screen.deleteMenuState,
            onDismissRequest = { controller.setDeleteMenuState(UiScreenState(UiState.COMPLETE)) }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Component.SearchBar(
                searchClue = screen.searchClue,
                onClueChanged = {
                    controller.updateSearchClue(it)
                },
                placeholder = stringResource(R.string.menu_name)
            )

            Spacer(modifier = Modifier.size(16.dp))

            NewMenuSection(
                newMenu = screen.newMenu,
                onMenuChanged = { controller.updateNewMenu(it) },
                onAlcoholCheckChanged = { controller.updateNewMenu(it) },
                onLunchCheckChanged = { controller.updateNewMenu(it) },
                onDinnerCheckChanged = { controller.updateNewMenu(it) },
                onMenuAdd = { controller.request { createMenu() } }
            )

            Spacer(modifier = Modifier.size(24.dp))

            Component.HandleUiStateDialog(
                uiState = screen.menuListState,
                onDismissRequest = { controller.setMenuListState(UiScreenState(UiState.COMPLETE)) },
                onRetryAction = { controller.retryUpdateMenuList() },
                onComplete = {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(
                            items = screen.menuList,
                            key = { it.name }
                        ) { menu ->
                            MenuCard(
                                menu = menu,
                                onDeleteClick = { controller.request { deleteMenu(menu) } }
                            )
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun NewMenuSection(
        newMenu: UiNewMenu,
        onMenuChanged: (UiNewMenu) -> Unit,
        onAlcoholCheckChanged: (UiNewMenu) -> Unit,
        onLunchCheckChanged: (UiNewMenu) -> Unit,
        onDinnerCheckChanged: (UiNewMenu) -> Unit,
        onMenuAdd: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.new_menu),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    label = { Text(text = stringResource(R.string.menu_name)) },
                    value = newMenu.name,
                    onValueChange = { onMenuChanged(newMenu.copy(name = it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(8.dp))

                TextField(
                    label = { Text(text = stringResource(R.string.menu_price)) },
                    value = newMenu.price,
                    onValueChange = { onMenuChanged(newMenu.copy(price = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = newMenu.isAlcohol,
                        onCheckedChange = { onAlcoholCheckChanged(newMenu.copy(isAlcohol = it)) }
                    )
                    Text(
                        text = "주류",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    Checkbox(
                        checked = newMenu.isLunch,
                        onCheckedChange = { onLunchCheckChanged(newMenu.copy(isLunch = it)) }
                    )
                    Text(
                        text = "런치",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    Checkbox(
                        checked = newMenu.isDinner,
                        onCheckedChange = { onDinnerCheckChanged(newMenu.copy(isDinner = it)) }
                    )
                    Text(
                        text = "디너",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = { onMenuAdd() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = stringResource(R.string.add_menu))
                }
            }
        }
    }

    @Composable
    fun MenuCard(
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