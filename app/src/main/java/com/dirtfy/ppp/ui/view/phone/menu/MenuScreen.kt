package com.dirtfy.ppp.ui.view.phone.menu

import android.view.RoundedCorner
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.menu.UiMenu
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import com.dirtfy.ppp.ui.view.phone.Component
import com.dirtfy.tagger.Tagger
import javax.inject.Inject


class MenuScreen @Inject constructor(
    val menuController: MenuController
): Tagger {

    @Composable
    fun Main(
        controller: MenuController = menuController
    ) {
        val screen by controller.uiMenuScreenState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.request { updateMenuList() }
        }

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
                placeholder = "menu name"
            )

            Spacer(modifier = Modifier.size(16.dp))

            NewMenuSection(
                newMenu = screen.newMenu,
                onMenuChanged = { controller.request { updateNewMenu(it) } },
                onMenuAdd = { controller.request { createMenu(it) } }
            )

            Spacer(modifier = Modifier.size(24.dp))

            when(screen.menuListState.state) {
                UiState.COMPLETE -> {
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

                UiState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                UiState.FAIL -> {

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = { controller.request { updateMenuList() } }) {
                                Text(text = "Retry")
                            }
                        },
                        title = { Text(text = screen.menuListState.failMessage ?: "unknown error") }
                    )
                }
            }
        }
    }

    @Composable
    fun NewMenuSection(
        newMenu: UiMenu,
        onMenuChanged: (UiMenu) -> Unit,
        onMenuAdd: (UiMenu) -> Unit
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
                    text = "Add New Menu",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    label = { Text(text = "Menu Name") },
                    value = newMenu.name,
                    onValueChange = { onMenuChanged(newMenu.copy(name = it)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(8.dp))

                TextField(
                    label = { Text(text = "Menu Price") },
                    value = newMenu.price,
                    onValueChange = { onMenuChanged(newMenu.copy(price = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = { onMenuAdd(newMenu) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Add Menu")
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
                    Text(text = "Price: ${menu.price}", style = MaterialTheme.typography.bodyMedium)
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