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
import com.dirtfy.ppp.common.DummyHomeViewModel
import com.dirtfy.ppp.contract.view.HomeViewContract
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract
import com.dirtfy.ppp.view.tablet.selling.tabling.TablingMainScreenPreview
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TabletScreen: HomeViewContract.API {
    @Composable
    override fun Navigator(
        destinationList: List<HomeViewModelContract.DTO.Screen>,
        nowPosition: HomeViewModelContract.DTO.Screen,
        viewModel: HomeViewModelContract.Navigator.API,
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

    private fun selectIcon(screen: HomeViewModelContract.DTO.Screen): ImageVector {
        return when(screen) {
            HomeViewModelContract.DTO.Screen.Tabling -> PPPIcons.Menu
            HomeViewModelContract.DTO.Screen.MenuManaging -> PPPIcons.Add
            HomeViewModelContract.DTO.Screen.Accounting -> PPPIcons.AccountBox
            HomeViewModelContract.DTO.Screen.SalesRecording -> PPPIcons.Build
            else -> PPPIcons.Close
        }
    }

    @Composable
    override fun NavGraph(
        navController: NavHostController,
        startDestination: HomeViewModelContract.DTO.Screen,
        viewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route
        ) {
            composable(route = HomeViewModelContract.DTO.Screen.Tabling.route) {

            }
        }
    }

    @Composable
    override fun Main(
        viewModel: HomeViewModelContract.API,
        modifier: Modifier
    ) {
        val destinationList = listOf(
            HomeViewModelContract.DTO.Screen.Tabling,
            HomeViewModelContract.DTO.Screen.MenuManaging,
            HomeViewModelContract.DTO.Screen.Accounting,
            HomeViewModelContract.DTO.Screen.SalesRecording
        )
        val navController = rememberNavController()

        Row(
            modifier = modifier
        ) {
            NavGraph(
                navController = navController,
                startDestination = HomeViewModelContract.DTO.Screen.Tabling,
                viewModel = viewModel,
                modifier = Modifier
            )

            Navigator(
                destinationList = destinationList,
                nowPosition = convertToScreen(
                    navController.currentDestination?.route?:
                    HomeViewModelContract.DTO.Screen.Tabling.route
                ),
                viewModel = viewModel,
                modifier = Modifier
            )
        }
    }

    private fun convertToScreen(
        route: String
    ): HomeViewModelContract.DTO.Screen {
        return when(route) {
            HomeViewModelContract.DTO.Screen.MenuManaging.route ->
                HomeViewModelContract.DTO.Screen.MenuManaging
            HomeViewModelContract.DTO.Screen.SalesRecording.route ->
                HomeViewModelContract.DTO.Screen.SalesRecording
            HomeViewModelContract.DTO.Screen.Accounting.route ->
                HomeViewModelContract.DTO.Screen.Accounting
            else -> HomeViewModelContract.DTO.Screen.Tabling
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun TabletScreenPreview() {
    PPPTheme {
        val destinationList = listOf(
            HomeViewModelContract.DTO.Screen.Tabling,
            HomeViewModelContract.DTO.Screen.MenuManaging,
            HomeViewModelContract.DTO.Screen.Accounting,
            HomeViewModelContract.DTO.Screen.SalesRecording
        )

        Row(
            modifier = Modifier
        ) {
            TablingMainScreenPreview()

            Spacer(Modifier.size(10.dp))

            TabletScreen.Navigator(
                destinationList = destinationList,
                nowPosition = HomeViewModelContract.DTO.Screen.Tabling,
                viewModel = DummyHomeViewModel,
                modifier = Modifier
            )
        }
    }
}