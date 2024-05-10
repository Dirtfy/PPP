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
import com.dirtfy.ppp.accounting.accountRecording.model.AccountRecordData
import com.dirtfy.ppp.accounting.accountRecording.viewmodel.AccountRecordListViewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.viewmodel.AccountViewModel
import com.dirtfy.ppp.ui.theme.PPPTheme
import com.google.firebase.Timestamp
import java.util.Date

object AccountRecordTestScreen{
    const val TAG = "AccountTestScreen"
}


@Composable
fun AccountRecordList(accountRecords: List<AccountRecordData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = accountRecords,
            key = { item ->
                item.recordID?:""
            }
        ) { item ->
            AccountRecordItem(data = item)
        }
    }
}

@Composable
fun AccountRecordItem(data: AccountRecordData) {
    Column{
        Text(text = "ID: ${data.recordID?:"null"}")
        Text(text = "Account ID: ${data.accountID}")
        Text(text = "Timestamp:" + data.timestamp.toString())
        Text(text = "User Name: "+ data.userName)
        Text(text = "Amount:" + data.amount.toString())
        Text(text = "Result:" + data.result.toString())
    }
}

@Composable
fun AccountRecordTest(
    arrivedAccountData: AccountData,
    accountViewModel: AccountViewModel = viewModel(),
    accountRecordListViewModel: AccountRecordListViewModel = viewModel()
) {

    // TODO : 일단은 이렇게...
    accountViewModel.forceDataInjection(arrivedAccountData)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        var accountID by remember {
            mutableStateOf("")
        }
        val accountData by accountViewModel.data.collectAsStateWithLifecycle(arrivedAccountData)
        Column {
            TextField(value = accountID, onValueChange = {accountID = it}, label = {
                Text(text = "account ID")
            })
            Button(onClick = {
                accountViewModel.reloadData {
                    it.accountID == accountID
                }
            }) {
                Text(text = "load account")
            }

            AccountItem(data = accountData)
        }

        Column {
            var userName by remember {
                mutableStateOf("")
            }
            TextField(value = userName, onValueChange = {userName = it}, label = {
                Text(text = "User Name")
            })
            var amount by remember {
                mutableStateOf("")
            }
            TextField(value = amount, onValueChange = {amount = it}, label = {
                Text(text = "amount")
            })
            Row{
                Button(onClick = {
                    // TODO: record insert 와 account update 가 둘다 동시에 성공하도록 구성해야함
                    accountRecordListViewModel.insertData(
                        AccountRecordData(
                            recordID = null,
                            accountID = accountData.accountID?:"error!!",
                            timestamp = Timestamp(Date()),
                            userName = userName,
                            amount = amount.toInt(),
                            result = amount.toInt()+accountData.balance
                        )
                    )
                    val newAccountData = accountData.copy(balance = accountData.balance+amount.toInt())
                    accountViewModel.updateData(newAccountData)
                }) {
                    Text(text = "create")
                }

                Button(onClick = {
                    accountRecordListViewModel.reloadData {
                        it.accountID == accountData.accountID
                    }
                }) {
                    Text(text = "reload All")
                }

                Button(onClick = { accountRecordListViewModel.clearData() }) {
                    Text(text = "clear")
                }
            }

        }

        val accountRecordData by accountRecordListViewModel.dataList.collectAsStateWithLifecycle(listOf())
        AccountRecordList(accountRecords = accountRecordData)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountRecordTestPreview() {
    PPPTheme {
        AccountRecordTest(AccountData())
    }
}