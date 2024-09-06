package com.dirtfy.ppp.ui.view.tablet

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dirtfy.ppp.test.ui.view.MainActivity
import com.dirtfy.ppp.ui.view.tablet.account.AccountScreen
import com.dirtfy.ppp.ui.view.tablet.menu.MenuScreen
import com.dirtfy.ppp.ui.view.tablet.record.RecordScreen
import com.dirtfy.ppp.ui.view.tablet.table.TableScreen

object TabletScreen {

    @Composable
    fun Main(
        navController: NavHostController,
        destinationList: List<Pair<String, ImageVector>>,
        selectedIndex: Int,
        onNavigatorItemClick: (Int) -> Unit
    ) {
        Row {
            NavHost(
                navController = navController,
                startDestination = MainActivity.Companion.Destination.Table.name
            ) {
                composable(route = MainActivity.Companion.Destination.Table.name) {
                    TableScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Menu.name) {
                    MenuScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Record.name) {
                    RecordScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Account.name) {
                    AccountScreen.Main()
                }
            }
            Navigator(
                selectedIndex = selectedIndex,
                destinationList = destinationList,
                onItemClick = onNavigatorItemClick
            )
        }

    }

    @Composable
    fun Navigator(
        selectedIndex: Int,
        destinationList: List<Pair<String, ImageVector>>,
        onItemClick: (Int) -> Unit
    ) {
        NavigationRail {
            destinationList.forEachIndexed { index, pair ->
                NavigationRailItem(
                    selected = selectedIndex == index,
                    onClick = { onItemClick(index) },
                    icon = {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first
                        )
                    }
                )
            }
        }
    }
}