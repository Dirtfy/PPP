package com.dirtfy.ppp.ui.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.ui.view.phone.PhoneScreen
import com.dirtfy.ppp.ui.view.tablet.TabletScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var phoneScreen : PhoneScreen
    @Inject
    lateinit var tabletScreen: TabletScreen

    companion object {
        enum class Destination {
            Table,
            Menu,
            Record,
            Account,
        }
    }

    private val isTablet: Boolean
        get() {
            val screenSizeType =
                resources.configuration.screenLayout and
                        Configuration.SCREENLAYOUT_SIZE_MASK

            return screenSizeType == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                    screenSizeType == Configuration.SCREENLAYOUT_SIZE_LARGE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            var selectedIndex by remember {
                mutableIntStateOf(0)
            }
            val destinationList by remember {
                mutableStateOf(
                    listOf(
                        Destination.Table.name to Icons.Filled.Home,
                        Destination.Menu.name to Icons.Filled.Menu,
                        Destination.Record.name to Icons.Filled.DateRange,
                        Destination.Account.name to Icons.Filled.AccountBox
                    )
                )
            }

            if (isTablet) {
                tabletScreen.Main(
                    navController = navController,
                    destinationList = destinationList,
                    selectedIndex = selectedIndex,
                    onNavigatorItemClick = {
                        selectedIndex = it
                        navController.navigate(destinationList[it].first) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            else {
                phoneScreen.Main(
                    navController = navController,
                    destinationList = destinationList,
                    selectedIndex = selectedIndex,
                    onNavigatorItemClick = {
                        selectedIndex = it
                        navController.navigate(destinationList[it].first) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}