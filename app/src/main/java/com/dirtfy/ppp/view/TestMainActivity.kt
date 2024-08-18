package com.dirtfy.ppp.view

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
import com.dirtfy.ppp.R
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account
import com.dirtfy.ppp.test.view.AccountRecordTest
import com.dirtfy.ppp.test.view.AccountTest
import com.dirtfy.ppp.test.view.MenuTest
import com.dirtfy.ppp.test.view.QRCodeGenerateTest
import com.dirtfy.ppp.test.view.QRCodeScanTest
import com.dirtfy.ppp.test.view.SalesRecordingTest
import com.dirtfy.ppp.test.view.TablingTestScreen
import com.dirtfy.ppp.test.view.TestMainScreen
import com.dirtfy.ppp.view.ui.theme.PPPTheme

class TestMainActivity : ComponentActivity() {

    object Const{
        var deepLinkScheme: String = "com.dirtfy.ppp"
        var deepLinkHost: String = "account_record"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Const.deepLinkScheme = getString(R.string.deeplink_scheme)
        Const.deepLinkHost = getString(R.string.deeplink_host)

        setContent {
            PPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestMainActivityScreen()
                }
            }
        }
    }
}

@Composable
fun TestMainActivityScreen() {
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
                        navController.navigate(
                            "${PPPScreen.AccountRecord.route}/" +
                                    PPPScreen.AccountRecord.buildArgumentString(it)
                        )},
                    navigateToMenuTest = { navController.navigate(PPPScreen.Menu.route) },
                    navigateToQRScanTest = { navController.navigate(PPPScreen.QRCodeScanTest.route) },
                    navigateToQRGenerateTest = { navController.navigate(PPPScreen.QRCodeGenerateTest.route) },
                    navigateToTablingTest = { navController.navigate(PPPScreen.TablingTest.route) },
                    navigateToSalesTest = { navController.navigate(PPPScreen.SalesTest.route) }
                )
            }
            composable(route = PPPScreen.Account.route) {
                AccountTest()
            }
            composable(
                route = PPPScreen.AccountRecord.routeWitchArgument,
                arguments = PPPScreen.AccountRecord.arguments,
                deepLinks = PPPScreen.AccountRecord.deepLinks
            ) {
                val arguments = requireNotNull(it.arguments)
                val accountData =
                    Account(
                        accountNumber = requireNotNull(arguments.getString(PPPScreen.AccountRecord.accountIDArg)),
                        accountName = requireNotNull(arguments.getString(PPPScreen.AccountRecord.accountNameArg)),
                        phoneNumber = requireNotNull(arguments.getString(PPPScreen.AccountRecord.phoneNumberArg)),
                        registerTimestamp = requireNotNull(arguments.getLong(PPPScreen.AccountRecord.registerTimestampArg)),
                        balance = requireNotNull(arguments.getInt(PPPScreen.AccountRecord.balanceArg))
                    )

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
            composable(route = PPPScreen.TablingTest.route) {
                TablingTestScreen()
            }
            composable(route = PPPScreen.SalesTest.route) {
                SalesRecordingTest()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PPPTheme {
        TestMainActivityScreen()
    }
}