package com.dirtfy.ppp.view.tablet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.ScreenContract
import com.dirtfy.ppp.view.tablet.selling.tabling.TablingMainScreenPreview
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TabletScreen: ScreenContract.API {
    @Composable
    override fun Navigator(
        destinationList: List<ScreenContract.DTO.Screen>,
        nowPosition: ScreenContract.DTO.Screen,
        user: User,
        modifier: Modifier
    ) {
        NavigationRail {
            destinationList.forEach {
                NavigationRailItem(
                    selected = (it == nowPosition),
                    onClick = { /*TODO*/ },
                    icon = {
                        val icon = selectIcon(it)

                        Icon(
                            imageVector = icon,
                            contentDescription = icon.name
                        )
                    }
                )
            }
        }
    }

    private fun selectIcon(screen: ScreenContract.DTO.Screen): ImageVector {
        return when(screen) {
            ScreenContract.DTO.Screen.Tabling -> PPPIcons.Menu
            ScreenContract.DTO.Screen.MenuManaging -> PPPIcons.Add
            ScreenContract.DTO.Screen.Accounting -> PPPIcons.AccountBox
            ScreenContract.DTO.Screen.SalesRecording -> PPPIcons.Build
            else -> PPPIcons.Close
        }
    }

    @Composable
    override fun NavGraph(
        navController: NavHostController,
        startDestination: ScreenContract.DTO.Screen,
        user: User,
        modifier: Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route
        ) {
            composable(route = ScreenContract.DTO.Screen.Tabling.route) {

            }
        }
    }

    @Composable
    override fun Main(user: User, modifier: Modifier) {
        val destinationList = listOf(
            ScreenContract.DTO.Screen.Tabling,
            ScreenContract.DTO.Screen.MenuManaging,
            ScreenContract.DTO.Screen.Accounting,
            ScreenContract.DTO.Screen.SalesRecording
        )
        val navController = rememberNavController()

        Row(
            modifier = modifier
        ) {
            NavGraph(
                navController = navController,
                startDestination = ScreenContract.DTO.Screen.Tabling,
                user = user,
                modifier = Modifier
            )

            Navigator(
                destinationList = destinationList,
                nowPosition = convertToScreen(
                    navController.currentDestination?.route?:
                    ScreenContract.DTO.Screen.Tabling.route
                ),
                user = user,
                modifier = Modifier
            )
        }
    }

    private fun convertToScreen(
        route: String
    ): ScreenContract.DTO.Screen {
        return ScreenContract.DTO.Screen.entries
            .filter { it.route == route }[0]
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun TabletScreenPreview() {
    PPPTheme {
        val destinationList = listOf(
            ScreenContract.DTO.Screen.Tabling,
            ScreenContract.DTO.Screen.MenuManaging,
            ScreenContract.DTO.Screen.Accounting,
            ScreenContract.DTO.Screen.SalesRecording
        )

        Row(
            modifier = Modifier
        ) {
            TablingMainScreenPreview()

            Spacer(Modifier.size(10.dp))

            TabletScreen.Navigator(
                destinationList = destinationList,
                nowPosition = ScreenContract.DTO.Screen.Tabling,
                user = DummyUser,
                modifier = Modifier
            )
        }
    }
}