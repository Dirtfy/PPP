package com.dirtfy.ppp.ui.view.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dirtfy.ppp.ui.view.MainActivity
import com.dirtfy.ppp.ui.view.tablet.account.AccountScreen
import com.dirtfy.ppp.ui.view.tablet.menu.MenuScreen
import com.dirtfy.ppp.ui.view.tablet.record.RecordScreen
import com.dirtfy.ppp.ui.view.tablet.table.TableScreen
import javax.inject.Inject

class TabletScreen @Inject constructor(
    val tableScreen: TableScreen,
    val menuScreen: MenuScreen,
    val recordScreen: RecordScreen,
    val accountScreen: AccountScreen
) {

    @Composable
    fun Main(
        navController: NavHostController,
        destinationList: List<Pair<String, ImageVector>>,
        selectedIndex: Int,
        onNavigatorItemClick: (Int) -> Unit
    ) {
        ConstraintLayout(
            modifier = Modifier.background(Color.White).fillMaxSize()
        ) {
            val (host, navigation) = createRefs()

            Navigator(
                selectedIndex = selectedIndex,
                destinationList = destinationList,
                onItemClick = onNavigatorItemClick,
                modifier = Modifier.constrainAs(navigation) {
                    start.linkTo(parent.start)
                }
            )

            NavHost(
                navController = navController,
                startDestination = MainActivity.Companion.Destination.Table.name,
                modifier = Modifier.constrainAs(host) {
                    start.linkTo(navigation.end)
                    end.linkTo(parent.end)

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                }.fillMaxHeight().padding(10.dp)
            ) {
                composable(route = MainActivity.Companion.Destination.Table.name) {
                    tableScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Menu.name) {
                    menuScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Record.name) {
                    recordScreen.Main()
                }
                composable(route = MainActivity.Companion.Destination.Account.name) {
                    accountScreen.Main()
                }
            }
        }
    }

    @Composable
    fun Navigator(
        selectedIndex: Int,
        destinationList: List<Pair<String, ImageVector>>,
        onItemClick: (Int) -> Unit,
        modifier: Modifier
    ) {
        NavigationRail(
            modifier = modifier
        ) {
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