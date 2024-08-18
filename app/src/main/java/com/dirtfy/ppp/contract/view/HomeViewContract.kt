package com.dirtfy.ppp.contract.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract

object HomeViewContract {

    interface API {

        @Composable
        fun Navigator(
            destinationList: List<HomeViewModelContract.DTO.Screen>,
            nowPosition: HomeViewModelContract.DTO.Screen,
            viewModel: HomeViewModelContract.Navigator.API,
            modifier: Modifier
        )

        @Composable
        fun NavGraph(
            navController: NavHostController,
            startDestination: HomeViewModelContract.DTO.Screen,
            viewModel: HomeViewModelContract.NavGraph.API,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: HomeViewModelContract.API,
            modifier: Modifier
        )
    }
}