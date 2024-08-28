package com.dirtfy.ppp.ui.view.phone

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dirtfy.ppp.ui.view.MainActivity
import com.dirtfy.ppp.ui.view.phone.account.AccountScreen
import com.dirtfy.ppp.ui.view.phone.menu.MenuScreen
import com.dirtfy.ppp.ui.view.phone.record.RecordScreen
import com.dirtfy.ppp.ui.view.phone.table.TableScreen

object PhoneScreen {

    @Composable
    fun Main(
        navController: NavHostController,
        destinationList: List<Pair<String, ImageVector>>,
        selectedIndex: Int,
        onNavigatorItemClick: (Int) -> Unit
    ) {
        Scaffold(
            bottomBar = {
                Navigator(
                    selectedIndex = selectedIndex,
                    destinationList = destinationList,
                    onItemClick = onNavigatorItemClick
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = MainActivity.Companion.Destination.Table.name,
                modifier = Modifier.padding(it).fillMaxSize()
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
        }
    }

    @Composable
    fun Navigator(
        selectedIndex: Int,
        destinationList: List<Pair<String, ImageVector>>,
        onItemClick: (Int) -> Unit
    ) {
        NavigationBar {
            destinationList.forEachIndexed { index, pair -> 
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { onItemClick(index) },
                    icon = {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first)
                    }
                )
            }
        }
    }
}