package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.viewmodel.AccountListViewModel
import com.dirtfy.ppp.ui.theme.PPPTheme
import java.util.Calendar

object AccountTestScreen{
    const val TAG = "AccountTestScreen"
}

@Composable
fun AccountCreate(
    number: String, onNumberChanged: (String) -> Unit,
    name: String, onNameChanged: (String) -> Unit,
    phoneNumber: String, onPhoneNumberChanged: (String) -> Unit,
    balance: String, onBalanceChanged: (String) -> Unit,
    onCreateButtonClick: () -> Unit
) {
    Column {
        TextField(
            value = number,
            onValueChange = { onNumberChanged(it) },
            label = { Text("account number") }
        )
        TextField(
            value = name,
            onValueChange = { onNameChanged(it) },
            label = { Text("account name") }
        )
        TextField(
            value = phoneNumber,
            onValueChange = { onPhoneNumberChanged(it) },
            label = { Text("phone number") }
        )
        TextField(
            value = balance,
            onValueChange = { onBalanceChanged(it) },
            label = { Text("balance") }
        )
        Button(onClick = onCreateButtonClick) {
            Text(text = "create")
        }
    }
}

@Composable
fun AccountList(accounts: List<AccountData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = accounts,
            key = { item ->
                item.accountNumber?:""
            }
        ) { item ->
            AccountItem(data = item)
        }
    }
}

@Composable
fun AccountItem(data: AccountData) {
    Column{
        Text(text = "ID: ${data.accountNumber?:"null"}")
        Text(text = "Name: "+ data.accountName)
        Text(text = "Balance:" + data.balance.toString())
        Text(text = "Timestamp:" + data.registerTimestamp.toString())
        Text(text = "Phone Number: "+ data.phoneNumber)
    }
}

@Composable
fun AccountTest(
    accountListViewModel: AccountListViewModel = viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        var number by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var balance by remember { mutableStateOf("") }

        AccountCreate(
            number = number,
            onNumberChanged = { number = it},
            name = name,
            onNameChanged = { name = it },
            phoneNumber = phoneNumber,
            onPhoneNumberChanged = { phoneNumber = it },
            balance = balance,
            onBalanceChanged = { balance = it }
        ) {
            accountListViewModel.insertData(
                AccountData(
                    accountNumber = number,
                    accountName = name,
                    phoneNumber = phoneNumber,
                    registerTimestamp = Calendar.getInstance().timeInMillis,
                    balance = balance.toInt()
                )
            )
            name = ""
            phoneNumber = ""
            balance = ""
        }

        Row {
            Button(onClick = {
                accountListViewModel.reloadData { true }
            }) {
                Text(text = "readAll")
            }
            Button(onClick = { accountListViewModel.clearData() }) {
                Text(text = "clear")
            }
        }

        val accountData by accountListViewModel.dataList.collectAsStateWithLifecycle(listOf())
        AccountList(accounts = accountData)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountTestPreview() {
    PPPTheme {
        MenuTest()
    }
}