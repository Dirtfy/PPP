package com.dirtfy.ppp.ui.view.tablet.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountDetailViewModel

object AccountDetailScreen {

    @Composable
    fun Main(
        account: UiAccount,
        controller: AccountDetailController = viewModel<AccountDetailViewModel>()
    ) {
        val screen by controller.uiAccountDetailScreenState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.request {
                updateNowAccount(account)
                updateAccountRecordList()
            }
        }

        ScreenContent(
            nowAccount = screen.nowAccount,
            newRecord = screen.newAccountRecord,
            recordList = screen.accountRecordList,
            recordListState = screen.accountRecordListState,
            onRecordChange = {
                controller.updateNewAccountRecord(it)
            },
            onAddClick = {
                controller.request { addRecord(it) }
            },
            onRetryClick = {
                controller.request { updateAccountRecordList() }
            }
        )
    }

    @Composable
    fun ScreenContent(
        nowAccount: UiAccount,
        newRecord: UiNewAccountRecord,
        recordList: List<UiAccountRecord>,
        recordListState: UiScreenState,
        onRecordChange: (UiNewAccountRecord) -> Unit,
        onAddClick: (UiNewAccountRecord) -> Unit,
        onRetryClick: () -> Unit
    ) {
        Surface(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AccountDetail(nowAccount = nowAccount)
                    Spacer(modifier = Modifier.size(10.dp))
                    NewRecordInput(
                        newRecord = newRecord,
                        onRecordChange = onRecordChange,
                        onAddClick = onAddClick
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    RecordListState(
                        recordList = recordList,
                        recordListState = recordListState,
                        onRetryClick = onRetryClick
                    )
                }
            }
        }
    }

    @Composable
    fun AccountDetail(
        nowAccount: UiAccount
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()
            ) {
                AccountSubData(nowAccount = nowAccount)

                Spacer(Modifier.size(20.dp))

                AccountMainData(nowAccount = nowAccount)
            }
        }
    }
    @Composable
    fun AccountSubData(
        nowAccount: UiAccount
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = nowAccount.number, fontSize = 12.sp)
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = nowAccount.registerTimestamp, fontSize = 12.sp)
            }
        }
    }
    @Composable
    fun AccountMainData(
        nowAccount: UiAccount
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = nowAccount.name)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = nowAccount.balance, fontSize = 25.sp)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = nowAccount.phoneNumber)
        }
    }

    @Composable
    fun NewRecordInput(
        newRecord: UiNewAccountRecord,
        onRecordChange: (UiNewAccountRecord) -> Unit,
        onAddClick: (UiNewAccountRecord) -> Unit
    ) {
        Card {
            Box(modifier = Modifier.padding(10.dp, 5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(0.8f)
                    ) {
                        IssuedNameInput(
                            newRecord = newRecord,
                            onRecordChange = onRecordChange
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        DifferenceInput(
                            newRecord = newRecord,
                            onRecordChange = onRecordChange
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                    }
                    IconButton(
                        onClick = { onAddClick(newRecord) },
                        modifier = Modifier.weight(0.2f)
                    ) {
                        val addIcon = Icons.Filled.Add
                        Icon(imageVector = addIcon, contentDescription = addIcon.name)
                    }
                }
            }

        }
    }
    @Composable
    fun IssuedNameInput(
        newRecord: UiNewAccountRecord,
        onRecordChange: (UiNewAccountRecord) -> Unit
    ) {
        TextField(
            label = { Text(text = "issued name") },
            value = newRecord.issuedName,
            onValueChange = {
                onRecordChange(newRecord.copy(issuedName = it))
            }
        )
    }
    @Composable
    fun DifferenceInput(
        newRecord: UiNewAccountRecord,
        onRecordChange: (UiNewAccountRecord) -> Unit
    ) {
        TextField(
            label = { Text(text = "difference") },
            value = newRecord.difference,
            onValueChange = {
                onRecordChange(newRecord.copy(difference = it))
            }
        )
    }

    @Composable
    fun RecordListState(
        recordList: List<UiAccountRecord>,
        recordListState: UiScreenState,
        onRetryClick: () -> Unit
    ) {
        when(recordListState.state) {
            UiState.COMPLETE -> {
                RecordList(recordList = recordList)
            }
            UiState.LOADING -> {
                RecordListLoading()
            }
            UiState.FAIL -> {
                val failMessage = recordListState.failMessage
                RecordListLoadFail(
                    failMessage = failMessage,
                    onRetryClick = onRetryClick
                )
            }
        }
    }

    @Composable
    fun RecordList(
        recordList: List<UiAccountRecord>
    ) {
        LazyColumn(
            modifier = Modifier.height(300.dp)
        ) {
            items(recordList) {
                ListItem(
                    overlineContent = { Text(text = it.timestamp) },
                    headlineContent = { Text(text = it.difference) },
                    supportingContent = { Text(text = it.result) },
                    trailingContent = { Text(text = it.issuedName) }
                )
            }
        }
    }

    @Composable
    fun RecordListLoading() {
        Column(
            modifier = Modifier.height(300.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxHeight()
            )
        }
    }

    @Composable
    fun RecordListLoadFail(
        failMessage: String?,
        onRetryClick: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = onRetryClick) {
                    Text(text = "Retry")
                }
            },
            dismissButton = {
                Button(onClick = { }) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = failMessage ?: "unknown error") }
        )
    }
}