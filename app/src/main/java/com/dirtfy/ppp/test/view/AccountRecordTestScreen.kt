package com.dirtfy.ppp.test.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.dirtfy.ppp.accounting.accountRecording.model.AccountRecordData
import com.dirtfy.ppp.accounting.accountRecording.viewmodel.AccountRecordListViewModel
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.viewmodel.AccountListViewModel
import com.dirtfy.ppp.ui.theme.PPPTheme
import com.google.firebase.Timestamp
import java.util.Date

object Const{
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
    accountListViewModel: AccountListViewModel = viewModel(),
    accountRecordListViewModel: AccountRecordListViewModel = viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        var accountID by remember {
            mutableStateOf("")
        }
        val accountDataList by accountListViewModel.dataList.observeAsState(listOf())
        val accountData = when(accountDataList.size) {
            0 -> AccountData(null, "test", "test",  Timestamp(Date()), 8888)
            else -> accountDataList[0]
        }
        Column {
            TextField(value = accountID, onValueChange = {accountID = it}, label = {
                Text(text = "account ID")
            })
            Button(onClick = {
                accountListViewModel.reloadData {
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
            Button(onClick = {
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
            }) {
                Text(text = "create")
            }
        }

        val accountRecordData by accountRecordListViewModel.dataList.observeAsState(listOf())
        AccountRecordList(accountRecords = accountRecordData)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountRecordTestPreview() {
    PPPTheme {
        AccountRecordTest()
    }
}