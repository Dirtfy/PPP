package com.dirtfy.ppp.view.phone.accounting.managing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirtfy.ppp.common.DummyAccountManagingViewModel
import com.dirtfy.ppp.contract.view.accounting.managing.AccountManagingViewContract
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object AccountManagingScreen: AccountManagingViewContract.API {
    @Composable
    override fun AccountDetail(
        account: AccountManagingViewModelContract.DTO.Account,
        viewModel: AccountManagingViewModelContract.AccountDetail.API,
        modifier: Modifier
    ) {
        ElevatedCard(
            modifier = modifier
        ) {
            Column {
                Text(text = account.number)
                Text(text = account.registerTimestamp)
            }

            Spacer(Modifier.size(35.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = account.name)
                Text(text = account.balance, fontSize = 35.sp)
                Text(text = account.phoneNumber)
            }
        }
    }

    @Composable
    override fun RecordList(
        recordList: List<AccountManagingViewModelContract.DTO.Record>,
        viewModel: AccountManagingViewModelContract.RecordList.API,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(recordList) {
                RecordItem(
                    record = it,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun RecordItem(
        record: AccountManagingViewModelContract.DTO.Record,
        viewModel: AccountManagingViewModelContract.RecordList.API,
        modifier: Modifier
    ) {
        ListItem(
            overlineContent = { Text(text = record.timestamp) },
            headlineContent = { Text(text = record.amount) },
            supportingContent = { Text(text = record.result) },
            trailingContent = { Text(text = record.userName) },
            modifier = modifier
        )
    }

    @Composable
    override fun NewRecord(
        record: AccountManagingViewModelContract.DTO.Record,
        viewModel: AccountManagingViewModelContract.NewRecord.API,
        modifier: Modifier
    ) {
        ElevatedCard(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    label = { Text(text = "user name") },
                    value = record.userName,
                    onValueChange = { /*TODO*/ }
                )

                Spacer(Modifier.size(10.dp))

                TextField(
                    label = { Text(text = "amount") },
                    value = record.amount,
                    onValueChange = { /*TODO*/ }
                )
            }
        }
    }

    @Composable
    override fun Main(
        startAccountDetail: AccountManagingViewModelContract.DTO.Account,
        viewModel: AccountManagingViewModelContract.API,
        modifier: Modifier
    ) {
        val currentAccount by viewModel.accountDetail
        val newRecord by viewModel.newRecord
        val recordList by viewModel.recordList

        viewModel.setAccountDetail(startAccountDetail)

        Column(
            modifier = modifier
        ) {
            AccountDetail(
                account = currentAccount,
                viewModel = viewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )

            NewRecord(
                record = newRecord,
                viewModel = viewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )

            RecordList(
                recordList = recordList,
                viewModel = viewModel,
                modifier = Modifier
            )
        }
    }

}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun AccountManagingScreenPreview() {
    val account = AccountManagingViewModelContract.DTO.Account(
        number = "18049",
        name = "test",
        phoneNumber = "010-8592-1485",
        registerTimestamp = "2024.07.08",
        balance = "105,300"
    )
    val recordList = MutableList(10) {
        AccountManagingViewModelContract.DTO.Record(
            timestamp = "2024.07.08",
            userName = "user_${it}",
            amount = "100,000",
            result = "105,300"
        )
    }
    val newRecord = AccountManagingViewModelContract.DTO.Record(
        timestamp = "2024.07.08",
        userName = "new user",
        amount = "100,000",
        result = "105,300"
    )

    PPPTheme {
        Column(
            modifier = Modifier
        ) {
            AccountManagingScreen.AccountDetail(
                account = account,
                viewModel = DummyAccountManagingViewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )

            AccountManagingScreen.NewRecord(
                record = newRecord,
                viewModel = DummyAccountManagingViewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )

            AccountManagingScreen.RecordList(
                recordList = recordList,
                viewModel = DummyAccountManagingViewModel,
                modifier = Modifier
            )
        }
    }
}