package com.dirtfy.ppp.view.phone

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.constraintlayout.compose.Dimension.Companion.wrapContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.common.DummyAccountingViewModel
import com.dirtfy.ppp.common.DummyHomeViewModel
import com.dirtfy.ppp.contract.view.HomeViewContract
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract
import com.dirtfy.ppp.view.phone.accounting.AccountingMainScreen
import com.dirtfy.ppp.view.phone.accounting.managing.AccountManagingScreen
import com.dirtfy.ppp.view.phone.selling.menu.managing.MenuManagingScreen
import com.dirtfy.ppp.view.phone.selling.recording.SalesRecordingScreen
import com.dirtfy.ppp.view.phone.selling.tabling.TablingMainScreen
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.dirtfy.ppp.viewmodel.use.accounting.AccountViewModel
import com.dirtfy.ppp.viewmodel.use.accounting.managing.AccountManagingViewModel
import com.dirtfy.ppp.viewmodel.use.selling.menu.managing.MenuViewModel
import com.dirtfy.ppp.viewmodel.use.selling.recording.SalesViewModel
import com.dirtfy.ppp.viewmodel.use.selling.tabling.TablingViewModel
import java.util.Calendar

object PhoneScreen: HomeViewContract.API {

    @Composable
    override fun Navigator(
        destinationList: List<HomeViewModelContract.DTO.Screen>,
        nowPosition: HomeViewModelContract.DTO.Screen,
        viewModel: HomeViewModelContract.Navigator.API,
        modifier: Modifier
    ) {
        NavigationBar(
            modifier = modifier
        ) {
            destinationList.forEach {
                NavigationBarItem(
                    selected = (it == nowPosition),
                    onClick = { viewModel.navigateTo(it) },
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
                TablingMainScreen.Main(
                    viewModel = viewModel<TablingViewModel>(),
                    modifier = modifier
                )
            }
            composable(route = HomeViewModelContract.DTO.Screen.MenuManaging.route) {
                MenuManagingScreen.Main(
                    viewModel = viewModel<MenuViewModel>(),
                    modifier = modifier
                )
            }
            composable(route = HomeViewModelContract.DTO.Screen.SalesRecording.route) {
                SalesRecordingScreen.Main(
                    viewModel = viewModel<SalesViewModel>(),
                    modifier = modifier
                )
            }
            composable(route = HomeViewModelContract.DTO.Screen.Accounting.route) {
                AccountingMainScreen.Main(
                    viewModel = viewModel<AccountViewModel>(),
                    homeViewModel = viewModel,
                    modifier = modifier
                )
            }
            composable(
                route = HomeViewModelContract.DTO.Screen.AccountManaging.routeWitchArgument,
                arguments = HomeViewModelContract.DTO.Screen.AccountManaging.arguments
            ) {
                val arguments = requireNotNull(it.arguments)

                val registerTimestamp = requireNotNull(
                    arguments.getLong(
                        HomeViewModelContract.DTO.Screen.AccountManaging.registerTimestampArg
                    )
                )
                val cal = Calendar.getInstance()
                cal.timeInMillis = registerTimestamp

                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)+1
                val day = cal.get(Calendar.DAY_OF_MONTH)

                val accountDetail =
                    AccountManagingViewModelContract.DTO.Account(
                        number = requireNotNull(
                            arguments.getString(
                                HomeViewModelContract.DTO.Screen.AccountManaging.accountNumberArg
                            )
                        ),
                        name = requireNotNull(
                            arguments.getString(
                                HomeViewModelContract.DTO.Screen.AccountManaging.accountNameArg
                            )
                        ),
                        phoneNumber = requireNotNull(
                            arguments.getString(
                                HomeViewModelContract.DTO.Screen.AccountManaging.phoneNumberArg
                            )
                        ),
                        registerTimestamp = "$year/$month/$day",
                        balance = requireNotNull(
                            arguments.getInt(
                                HomeViewModelContract.DTO.Screen.AccountManaging.balanceArg
                            )
                        ).toString()
                    )

                AccountManagingScreen.Main(
                    startAccountDetail = accountDetail,
                    viewModel = viewModel<AccountManagingViewModel>(),
                    modifier = modifier
                )
            }
        }
    }

    @Composable
    override fun Main(
        viewModel: HomeViewModelContract.API,
        modifier: Modifier
    ) {
        viewModel.setNavController(rememberNavController())

        ConstraintLayout(
            modifier = modifier
        ) {
            val (graph, navigator) = createRefs()

            Box(
                modifier = Modifier.constrainAs(graph) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(navigator.top)
                    height = fillToConstraints
                }
            ) {
                NavGraph(
                    navController = viewModel.navController,
                    startDestination = HomeViewModelContract.DTO.Screen.Tabling,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }

            Box(
                modifier = Modifier.constrainAs(navigator) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = wrapContent
                }
            ) {
                val nowPosition by viewModel.nowPosition
                Navigator(
                    destinationList = viewModel.destinationList,
                    nowPosition = nowPosition,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun TabletScreenPreview() {
    PPPTheme {
        val destinationList = listOf(
            HomeViewModelContract.DTO.Screen.Tabling,
            HomeViewModelContract.DTO.Screen.MenuManaging,
            HomeViewModelContract.DTO.Screen.Accounting,
            HomeViewModelContract.DTO.Screen.SalesRecording
        )

        val accountList = MutableList(10) {
            AccountingViewModelContract.DTO.Account(
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
                    viewModel = DummyAccountingViewModel,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                AccountingMainScreen.AccountList(
                    accountList = accountList,
                    viewModel = DummyAccountingViewModel,
                    homeViewModel = DummyHomeViewModel,
                    modifier = Modifier
                )
            }

            Spacer(Modifier.size(10.dp))

            PhoneScreen.Navigator(
                destinationList = destinationList,
                nowPosition = HomeViewModelContract.DTO.Screen.Tabling,
                viewModel = DummyHomeViewModel,
                modifier = Modifier
            )
        }
    }
}