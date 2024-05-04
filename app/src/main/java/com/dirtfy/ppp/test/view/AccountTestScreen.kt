package com.dirtfy.ppp.test.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.viewmodel.AccountListViewModel
import com.dirtfy.ppp.test.DClass
import com.dirtfy.ppp.ui.theme.PPPTheme
import com.google.firebase.Timestamp
import java.util.Date

const val TAG = "AccountTestScreen"

@Composable
fun AccountList(accounts: List<AccountData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = accounts,
            key = { item ->
                item.accountID?:""
            }
        ) { item ->
            AccountItem(data = item)
        }
    }
}

@Composable
fun AccountItem(data: AccountData) {
    Column{
        Text(text = "ID: ${data.accountID?:"null"}")
        Text(text = "Name: "+ data.accountName)
        Text(text = "Balance:" + data.balance.toString())
        Text(text = "Timestamp:" + data.registerTimestamp.toString())
    }
}

@Composable
fun AccountTest(
    accountListViewModel: AccountListViewModel = viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

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
                accountListViewModel.insertData(
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
            Button(onClick = {
                accountListViewModel.reloadData { true }
            }) {
                Text(text = "readAll")
            }
            Button(onClick = { accountListViewModel.clearData() }) {
                Text(text = "clear")
            }
        }

        val accountData by accountListViewModel.dataList.observeAsState(listOf())
        AccountList(accounts = accountData)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountTestPreview() {
    PPPTheme {
        AccountTest()
    }
}