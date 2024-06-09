package com.dirtfy.ppp.contract.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.dirtfy.ppp.contract.user.User

object ScreenContract {

    object DTO {
        enum class Screen(
            val route: String
        ) {
            Tabling("tabling"),
            SalesRecording("sales/recording"),
            MenuManaging("menu/managing"),
            Accounting("accounting"),
            AccountManaging("accounting/managing"),
        }
    }

    interface API {

        @Composable
        fun Navigator(
            destinationList: List<DTO.Screen>,
            nowPosition: DTO.Screen,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun NavGraph(
            navController: NavHostController,
            startDestination: DTO.Screen,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Main(
            user: User,
            modifier: Modifier
        )
    }
}