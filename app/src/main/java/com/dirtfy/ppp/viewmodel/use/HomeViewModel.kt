package com.dirtfy.ppp.viewmodel.use

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavHostController
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract

class HomeViewModel(
    application: Application
): AndroidViewModel(application), HomeViewModelContract.API {
    override val destinationList: List<HomeViewModelContract.DTO.Screen>
        get() = listOf(
            HomeViewModelContract.DTO.Screen.Tabling,
            HomeViewModelContract.DTO.Screen.MenuManaging,
            HomeViewModelContract.DTO.Screen.SalesRecording,
            HomeViewModelContract.DTO.Screen.Accounting
        )

    private val _nowPosition: MutableState<HomeViewModelContract.DTO.Screen>
    = mutableStateOf(HomeViewModelContract.DTO.Screen.Tabling)
    override val nowPosition: State<HomeViewModelContract.DTO.Screen>
        get() = _nowPosition

    override fun navigateTo(
        screen: HomeViewModelContract.DTO.Screen
    ) {
        navController.navigate(route = screen.route)
    }
    override fun navigateTo(
        screen: HomeViewModelContract.DTO.Screen,
        argumentString: String
    ) {
        navController.navigate(route = screen.route+"/$argumentString")
    }

    override val startDestination: HomeViewModelContract.DTO.Screen
        get() = HomeViewModelContract.DTO.Screen.Tabling

    private var _navController: NavHostController? = null
    override val navController: NavHostController
        get() = _navController?: throw NullPointerException()

    override fun setNavController(navController: NavHostController) {
        _navController = navController
    }

}