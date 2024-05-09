package com.dirtfy.ppp

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class PPPScreen(val route: String) {
    data object Start: PPPScreen("start")
    data object Order: PPPScreen("order")
    data object Menu: PPPScreen("menu")
    data object Account: PPPScreen("account")

    // TODO: route R.string.deeplink_host에서 가져오기
    data object AccountRecord: PPPScreen("account_record") {
        const val accountTypeArg = "account_data"
        val routeWitchArgument = "$route/{$accountTypeArg}"
        val arguments = listOf(
            navArgument(accountTypeArg) { type = NavType.StringType }
        )
        val deepLinks = listOf(
            navDeepLink { uriPattern = "ppp://$route/{$accountTypeArg}"}
        )
    }

    data object QRCodeGenerateTest: PPPScreen("qr_generate")
    data object QRCodeScanTest: PPPScreen("qr_scan")
}