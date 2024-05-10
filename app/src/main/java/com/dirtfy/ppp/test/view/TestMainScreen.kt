package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.accounting.accounting.model.AccountData

@Composable
fun TestMainScreen(
    navigateToAccountTest: () -> Unit,
    navigateToRecordTest: (AccountData) -> Unit,
    navigateToMenuTest: () -> Unit,
    navigateToQRScanTest: () -> Unit,
    navigateToQRGenerateTest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = navigateToAccountTest) {
            Text(text = "account test")
        }
        Button(onClick = { navigateToRecordTest(AccountData(accountID = "test")) }) {
            Text(text = "account record test")
        }
        Button(onClick = navigateToMenuTest) {
            Text(text = "menu test")
        }
        Button(onClick = { navigateToQRScanTest() }) {
            Text(text = "QR Code scan test")
        }
        Button(onClick = { navigateToQRGenerateTest() }) {
            Text(text = "QR Code generate test")
        }
    }
}