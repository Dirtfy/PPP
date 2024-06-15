package com.dirtfy.ppp.view.tablet.accounting.managing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.view.accounting.managing.AccountManagingScreenContract
import com.dirtfy.ppp.contract.viewmodel.AccountManagingContract
import com.dirtfy.ppp.contract.viewmodel.user.DummyUser
import com.dirtfy.ppp.contract.viewmodel.user.User
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object AccountManagingScreen: AccountManagingScreenContract.API {
    @Composable
    override fun AccountDetail(
        account: AccountManagingContract.DTO.Account,
        user: User,
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
        recordList: List<AccountManagingContract.DTO.Record>,
        user: User,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = modifier
        ) {
            items(recordList) {
                RecordItem(
                    record = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun RecordItem(
        record: AccountManagingContract.DTO.Record,
        user: User,
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
    override fun Record(
        record: AccountManagingContract.DTO.Record,
        user: User,
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


                Spacer(Modifier.size(10.dp))

                IconButton(onClick = { /*TODO*/ }) {
                    val addIcon = PPPIcons.Add
                    Icon(
                        imageVector = addIcon,
                        contentDescription = addIcon.name
                    )
                }
            }
        }
    }

    @Composable
    override fun Main(
        viewModel: AccountManagingContract.API,
        user: User,
        modifier: Modifier
    ) {
        val recordList by viewModel.recordList.collectAsStateWithLifecycle()
        val currentAccount by viewModel.currentAccount.collectAsStateWithLifecycle()
        val newRecord by viewModel.newRecord.collectAsStateWithLifecycle()

        Row(
            modifier = modifier
        ) {
            RecordList(
                recordList = recordList,
                user = user,
                modifier = Modifier.widthIn(800.dp, 1000.dp)
            )

            Column {
                AccountDetail(
                    account = currentAccount,
                    user = user,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

                Record(
                    record = newRecord,
                    user = user,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun AccountManagingScreenPreview() {
    val account = AccountManagingContract.DTO.Account(
        number = "18049",
        name = "test",
        phoneNumber = "010-8592-1485",
        registerTimestamp = "2024.07.08",
        balance = "105,300"
    )
    val recordList = MutableList(10) {
        AccountManagingContract.DTO.Record(
            timestamp = "2024.07.08",
            userName = "user_${it}",
            amount = "100,000",
            result = "105,300"
        )
    }
    val newRecord = AccountManagingContract.DTO.Record(
        timestamp = "2024.07.08",
        userName = "new user",
        amount = "100,000",
        result = "105,300"
    )

    PPPTheme {
        Row(
            modifier = Modifier
        ) {
            AccountManagingScreen.RecordList(
                recordList = recordList,
                user = DummyUser,
                modifier = Modifier.widthIn(800.dp, 1000.dp)
            )

            Column {
                AccountManagingScreen.AccountDetail(
                    account = account,
                    user = DummyUser,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

                AccountManagingScreen.Record(
                    record = newRecord,
                    user = DummyUser,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}