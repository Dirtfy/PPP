package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract

object DummyHomeViewModel: HomeViewModelContract.API {
    override val destinationList: List<HomeViewModelContract.DTO.Screen>
        get() = TODO("Not yet implemented")
    override val nowPosition: State<HomeViewModelContract.DTO.Screen>
        get() = TODO("Not yet implemented")

    override fun navigateTo(screen: HomeViewModelContract.DTO.Screen) {
        TODO("Not yet implemented")
    }

    override fun navigateTo(screen: HomeViewModelContract.DTO.Screen, argumentString: String) {
        TODO("Not yet implemented")
    }

    override val startDestination: HomeViewModelContract.DTO.Screen
        get() = TODO("Not yet implemented")
    override val navController: NavHostController
        get() = TODO("Not yet implemented")

    override fun setNavController(navController: NavHostController) {
        TODO("Not yet implemented")
    }
}