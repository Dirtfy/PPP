package com.dirtfy.ppp.accounting.accounting.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.viewmodel.AccountViewModel
import com.dirtfy.ppp.ui.theme.PPPTheme
import com.google.firebase.Timestamp
import java.util.Date

class AccountTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccountTest()
                }
            }
        }
    }
}

@Composable
fun ColumnItem(data: AccountData) {
    Column{
        Text(text = data.accountID?:"null")
        Text(text = data.accountName)
        Text(text = data.balance.toString())
    }
}

@Composable
fun SimpleTextField(text: String, label: String) {



}

@Composable
fun AccountTest(accountViewModel: AccountViewModel = viewModel()) {
    Column {
        var nameText by remember { mutableStateOf("") }
        TextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("account name") }
        )

        var pnText by remember { mutableStateOf("") }
        TextField(
            value = pnText,
            onValueChange = { pnText = it },
            label = { Text("phone number") }
        )

        var balance by remember { mutableStateOf("") }
        TextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("balance") }
        )

        Row {
            Button(onClick = {
                accountViewModel.createAccount(
                    AccountData(
                        accountID = null,
                        accountName = nameText,
                        phoneNumber = pnText,
                        registerTimestamp = Timestamp(Date()),
                        balance = balance.toInt()
                    )
                )
                nameText = ""
                pnText = ""
                balance = ""
            }) {
                Text(text = "create")
            }
            Button(onClick = { }) {
                Text(text = "readAll")
            }
        }
        LazyColumn {
            itemsIndexed(
                accountViewModel.viewState.value.accountDataList
            ) { index, item ->
                ColumnItem(data = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountTestPreview() {
    PPPTheme {
        AccountTest()
    }
}