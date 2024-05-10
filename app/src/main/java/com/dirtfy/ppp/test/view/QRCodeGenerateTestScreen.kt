package com.dirtfy.ppp.test.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.accounting.accountRecording.barcoding.viewmodel.QRCodeViewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import java.util.Calendar

@Composable
fun QRCodeGenerateTest(
    qrCodeViewModel: QRCodeViewModel = viewModel()
){
    Column {
        var name by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var balance by remember { mutableStateOf("") }

        AccountCreate(
            name = name,
            onNameChanged = { name = it },
            phoneNumber = phoneNumber,
            onPhoneNumberChanged = { phoneNumber = it },
            balance = balance,
            onBalanceChanged = { balance = it }
        ) {
            val accountData = AccountData(
                accountID = "test",
                accountName = name,
                phoneNumber = phoneNumber,
                registerTimestamp = Calendar.getInstance().timeInMillis,
                balance = balance.toInt()
            )
            qrCodeViewModel.generate(accountData)
        }

        val qrBitmap by qrCodeViewModel.QRCode.collectAsStateWithLifecycle()
        Image(bitmap = qrBitmap, contentDescription = "QRCode")
    }

}