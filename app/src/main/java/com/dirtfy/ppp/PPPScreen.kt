package com.dirtfy.ppp

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.dirtfy.ppp.accounting.accounting.model.AccountData

sealed class PPPScreen(val route: String) {
    data object Start: PPPScreen("start")
    data object Order: PPPScreen("order")
    data object Menu: PPPScreen("menu")
    data object Account: PPPScreen("account")

    // TODO: route R.string.deeplink_host에서 가져오기
    data object AccountRecord: PPPScreen("account_record") {
        const val accountIDArg = "account_id"
        const val accountNameArg = "account_name"
        const val phoneNumberArg = "phone_number"
        const val registerTimestampArg = "register_timestamp"
        const val balanceArg = "balance"

        val routeWitchArgument = "$route/" +
                "account_id={$accountIDArg}&" +
                "account_name={$accountNameArg}&" +
                "phone_number={$phoneNumberArg}&" +
                "register_timestamp={$registerTimestampArg}&" +
                "balance={$balanceArg}"
        val arguments = listOf(
            navArgument(accountIDArg) { type = NavType.StringType },
            navArgument(accountNameArg) { type = NavType.StringType },
            navArgument(phoneNumberArg) { type = NavType.StringType },
            navArgument(registerTimestampArg) { type = NavType.LongType },
            navArgument(balanceArg) { type = NavType.IntType },
        )
        val deepLinks = listOf(
            navDeepLink {
                uriPattern = "${MainActivity.Const.deepLinkScheme}://" +
                        routeWitchArgument
            }
        )

        fun buildArgumentString(data: AccountData): String {
            return "account_id=${data.accountID}&" +
                    "account_name=${data.accountName}&" +
                    "phone_number=${data.phoneNumber}&" +
                    "register_timestamp=${data.registerTimestamp}&" +
                    "balance=${data.balance}"
        }
    }

    data object QRCodeGenerateTest: PPPScreen("qr_generate")
    data object QRCodeScanTest: PPPScreen("qr_scan")
}