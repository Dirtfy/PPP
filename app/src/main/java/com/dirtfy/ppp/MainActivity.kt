package com.dirtfy.ppp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.test.view.AccountRecordTest
import com.dirtfy.ppp.test.view.AccountTest
import com.dirtfy.ppp.test.view.MenuTest
import com.dirtfy.ppp.test.view.TestMainScreen
import com.dirtfy.ppp.ui.theme.PPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PPPScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PPPScreen.Start.name) {
                TestMainScreen(
                    navigateToAccountTest = { navController.navigate(PPPScreen.Account.name) },
                    navigateToRecordTest = {navController.navigate(PPPScreen.AccountRecord.name)},
                    navigateToMenuTest = {navController.navigate(PPPScreen.Menu.name)})
            }
            composable(route = PPPScreen.Account.name) {
                AccountTest()
            }
            composable(route = PPPScreen.AccountRecord.name) {
                AccountRecordTest()
            }
            composable(route = PPPScreen.Menu.name) {
                MenuTest()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PPPTheme {
        MainScreen()
    }
}