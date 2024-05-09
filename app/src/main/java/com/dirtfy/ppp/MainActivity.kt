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
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.test.view.AccountRecordTest
import com.dirtfy.ppp.test.view.AccountTest
import com.dirtfy.ppp.test.view.MenuTest
import com.dirtfy.ppp.test.view.QRCodeGenerateTest
import com.dirtfy.ppp.test.view.QRCodeScanTest
import com.dirtfy.ppp.test.view.TestMainScreen
import com.dirtfy.ppp.ui.theme.PPPTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
            startDestination = PPPScreen.Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PPPScreen.Start.route) {
                TestMainScreen(
                    navigateToAccountTest = { navController.navigate(PPPScreen.Account.route) },
                    navigateToRecordTest = {
                        navController.navigate("${PPPScreen.AccountRecord.route}/$")
                                           },
                    navigateToMenuTest = { navController.navigate(PPPScreen.Menu.route) })
            }
            composable(route = PPPScreen.Account.route) {
                AccountTest()
            }
            composable(
                route = PPPScreen.AccountRecord.routeWitchArgument,
                arguments = PPPScreen.AccountRecord.arguments,
                deepLinks = PPPScreen.AccountRecord.deepLinks
            ) {
                val serializedAccountData =
                    it.arguments?.getString(PPPScreen.AccountRecord.accountTypeArg)?:
                    Json.encodeToString(AccountData())
                val accountData = Json.decodeFromString<AccountData>(serializedAccountData)

                AccountRecordTest(accountData)
            }
            composable(route = PPPScreen.Menu.route) {
                MenuTest()
            }
            composable(route = PPPScreen.QRCodeGenerateTest.route) {
                QRCodeGenerateTest()
            }
            composable(route = PPPScreen.QRCodeScanTest.route) {
                QRCodeScanTest()
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