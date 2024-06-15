package com.dirtfy.ppp.view.phone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.contract.view.ScreenContract
import com.dirtfy.ppp.contract.viewmodel.AccountingContract
import com.dirtfy.ppp.contract.viewmodel.user.DummyUser
import com.dirtfy.ppp.contract.viewmodel.user.User
import com.dirtfy.ppp.view.phone.accounting.AccountingMainScreen
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object PhoneScreen: ScreenContract.API {

    @Composable
    override fun Navigator(
        destinationList: List<ScreenContract.DTO.Screen>,
        nowPosition: ScreenContract.DTO.Screen,
        user: User,
        modifier: Modifier
    ) {
        NavigationBar(
            modifier = modifier
        ) {
            destinationList.forEach {
                NavigationBarItem(
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun TabletScreenPreview() {
    PPPTheme {
        val destinationList = listOf(
            ScreenContract.DTO.Screen.Tabling,
            ScreenContract.DTO.Screen.MenuManaging,
            ScreenContract.DTO.Screen.Accounting,
            ScreenContract.DTO.Screen.SalesRecording
        )

        val accountList = MutableList(10) {
            AccountingContract.DTO.Account(
                number = "$it",
                name = "test_$it",
                registerDate = "2024.6.${it+1}"
            )
        }
        val searchClue = ""

        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.heightIn(600.dp, 750.dp)
            ) {
                AccountingMainScreen.SearchBar(
                    searchClue = searchClue,
                    user = DummyUser,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                AccountingMainScreen.AccountList(
                    accountList = accountList,
                    user = DummyUser,
                    modifier = Modifier
                )
            }

            Spacer(Modifier.size(10.dp))

            PhoneScreen.Navigator(
                destinationList = destinationList,
                nowPosition = ScreenContract.DTO.Screen.Tabling,
                user = DummyUser,
                modifier = Modifier
            )
        }
    }
}