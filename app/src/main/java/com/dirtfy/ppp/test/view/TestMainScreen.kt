package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account

@Composable
fun TestMainScreen(
    navigateToAccountTest: () -> Unit,
    navigateToRecordTest: (Account) -> Unit,
    navigateToMenuTest: () -> Unit,
    navigateToQRScanTest: () -> Unit,
    navigateToQRGenerateTest: () -> Unit,
    navigateToTablingTest: () -> Unit,
    navigateToSalesTest: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = navigateToAccountTest) {
            Text(text = "account test")
        }
        Button(onClick = { navigateToRecordTest(Account(accountNumber = "test")) }) {
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
        Button(onClick = { navigateToTablingTest() }) {
            Text(text = "tabling test")
        }
        Button(onClick = { navigateToSalesTest() }) {
            Text(text = "sales test")
        }
    }
}