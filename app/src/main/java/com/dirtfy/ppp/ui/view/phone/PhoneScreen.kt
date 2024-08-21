package com.dirtfy.ppp.ui.view.phone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.ui.view.MainActivity
import com.dirtfy.ppp.ui.view.phone.account.AccountScreen
import com.dirtfy.ppp.ui.view.phone.menu.MenuScreen

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
                startDestination = MainActivity.Companion.Destination.Menu.name,
                modifier = Modifier.padding(it)
            ) {
                composable(route = MainActivity.Companion.Destination.Table.name) {

                }
                composable(route = MainActivity.Companion.Destination.Menu.name) {
                    MenuScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Sales.name) {

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