package com.dirtfy.ppp.contract.viewmodel

import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

object HomeViewModelContract {

    object DTO {
        sealed class Screen(
            val route: String
        ) {
            data object Tabling: Screen("tabling")
            data object SalesRecording: Screen("sales/recording")
            data object MenuManaging: Screen("menu/managing")
            data object Accounting: Screen("accounting")
            data object AccountManaging: Screen("accounting/managing") {
                val accountNumberArg = "account_number"
                val accountNameArg = "account_name"
                val phoneNumberArg = "phone_number"
                val registerTimestampArg = "register_timestamp"
                val balanceArg = "balance"

                val routeWitchArgument = "${route}/" +
                        "account_number={${accountNumberArg}}&" +
                        "account_name={${accountNameArg}}&" +
                        "phone_number={${phoneNumberArg}}&" +
                        "register_timestamp={${registerTimestampArg}}&" +
                        "balance={${balanceArg}}"

                val arguments = listOf(
                    navArgument(accountNumberArg) { type = NavType.StringType },
                    navArgument(accountNameArg) { type = NavType.StringType },
                    navArgument(phoneNumberArg) { type = NavType.StringType },
                    navArgument(registerTimestampArg) { type = NavType.LongType },
                    navArgument(balanceArg) { type = NavType.IntType },
                )
            }
        }


    }

    object Navigator {
        interface API {
            val destinationList: List<DTO.Screen>
            val nowPosition: State<DTO.Screen>

            fun navigateTo(screen: DTO.Screen)
            fun navigateTo(screen: DTO.Screen, argumentString: String)
        }
    }

    object NavGraph {
        interface API {
            val startDestination: DTO.Screen
            val navController: NavHostController

            fun setNavController(navController: NavHostController)
            fun navigateTo(screen: DTO.Screen)
            fun navigateTo(screen: DTO.Screen, argumentString: String)
        }
    }

    interface API: Navigator.API, NavGraph.API
}