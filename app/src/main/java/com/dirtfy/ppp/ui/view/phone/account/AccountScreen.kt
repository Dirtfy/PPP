package com.dirtfy.ppp.ui.view.phone.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.AccountAddController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountAddViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountDetailViewModel
import com.dirtfy.ppp.ui.presenter.viewmodel.account.AccountViewModel
import com.dirtfy.ppp.ui.view.phone.Component
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

object AccountScreen {

    @Composable
    fun Main(
        controller: AccountController = viewModel<AccountViewModel>()
    ) {
        val searchClue by controller.searchClue.collectAsStateWithLifecycle()
        val accountListState by controller.accountList.collectAsStateWithLifecycle()

        val nowAccount by controller.nowAccount.collectAsStateWithLifecycle()

        val isAccountCreateMode by controller.isAccountCreateMode.collectAsStateWithLifecycle()
        val isAccountUpdateMode by controller.isAccountUpdateMode.collectAsStateWithLifecycle()
        val isAccountDetailMode by controller.isAccountDetailMode.collectAsStateWithLifecycle()

        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            controller.request {
                updateSearchClue(it.contents?:"")
            }
        }

        LaunchedEffect(key1 = controller) {
            controller.request { updateAccountList() }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                searchClue = searchClue,
                onClueChanged = { controller.request { updateSearchClue(it) } },
                onBarcodeIconClick = {
                    scanLauncher.launch(
                        ScanOptions().setOrientationLocked(false)
                    )
                },
                onAddIconClick = { controller.request { setAccountCreateMode(true) } }
            )
            Spacer(modifier = Modifier.size(10.dp))

            when(accountListState) {
                is FlowState.Success -> {
                    val accountList = (accountListState as FlowState.Success<List<UiAccount>>).data
                    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
                        val moreIcon = Icons.Filled.MoreVert
                        items(accountList) {
                            ListItem(
                                overlineContent = { Text(text = it.number) },
                                headlineContent = { Text(text = it.name) },
                                supportingContent = { Text(text = it.phoneNumber) },
                                trailingContent = {
                                    Icon(
                                        imageVector = moreIcon,
                                        contentDescription = moreIcon.name
                                    )
                                },
                                modifier = Modifier.clickable {
                                    controller.request {
                                        updateNowAccount(it)
                                        setAccountDetailMode(true)
                                    }
                                }
                            )
                        }
                    }
                }
                is FlowState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is FlowState.Failed -> {
                    val throwable = (accountListState as FlowState.Failed<List<UiAccount>>).throwable

                    FailDialog(throwable = throwable) {
                        controller.request { updateAccountList() }
                    }
                }
            }
            
            if (isAccountCreateMode) {
                AccountCreateDialog(onDismissRequest = {
                    controller.request { setAccountCreateMode(false) }
                })
            }
            if (isAccountUpdateMode) {

            }
            if (isAccountDetailMode) {
                AccountDetailDialog(
                    account = nowAccount,
                    onDismissRequest = {
                    controller.request { setAccountDetailMode(false) }
                })
            }

        }

    }

    @Composable
    fun Loading() {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun FailDialog(
        throwable: Throwable,
        onOk: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = onOk) {
                    Text(text = "OK")
                }
            },
            title = { Text(text = throwable.message?: "unknown error") }
        )
    }

    @Composable
    fun SearchBar(
        searchClue: String,
        onClueChanged: (String) -> Unit,
        onBarcodeIconClick: () -> Unit,
        onAddIconClick: () -> Unit
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.padding(20.dp, 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .wrapContentWidth()
                ) {
                    val searchIcon = Icons.Filled.Search
                    Icon(imageVector = searchIcon, contentDescription = searchIcon.name)
                    TextField(
                        value = searchClue,
                        onValueChange = onClueChanged,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text(text = "account number")},
                        modifier = Modifier.widthIn(200.dp, 400.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    val barcodeIcon = Icons.Filled.Menu
                    Icon(
                        imageVector = barcodeIcon, contentDescription = barcodeIcon.name,
                        modifier = Modifier
                            .rotate(90f)
                            .clickable {
                                onBarcodeIconClick()
                            }
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    val addIcon = Icons.Filled.AddCircle
                    Icon(
                        imageVector = addIcon, contentDescription = addIcon.name,
                        modifier = Modifier.clickable {
                            onAddIconClick()
                        }
                    )
                }
            }
        }


    }
    
    @Composable
    fun AccountCreateDialog(
        onDismissRequest: () -> Unit,
        controller: AccountAddController = viewModel<AccountAddViewModel>()
    ) {
        val newAccount by controller.newAccount.collectAsStateWithLifecycle()

        Dialog(onDismissRequest = onDismissRequest) {
            Card {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        TextField(
                            label = { Text(text = "account number") },
                            value = newAccount.number,
                            onValueChange = {
                                controller.request {
                                    updateNewAccount(newAccount.copy(number = it))
                                }
                            },
                            trailingIcon = {
                                val randomIcon = Icons.Filled.Refresh
                                Icon(
                                    imageVector = randomIcon,
                                    contentDescription = randomIcon.name,
                                    modifier = Modifier.clickable {
                                        controller.request {
                                            setRandomValidAccountNumberToNewAccount()
                                        }
                                    }
                                )
                            },
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        TextField(
                            label = { Text(text = "account name") },
                            value = newAccount.name,
                            onValueChange = {
                                controller.request {
                                    updateNewAccount(newAccount.copy(name = it))
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        TextField(
                            label = { Text(text = "phone number") },
                            value = newAccount.phoneNumber,
                            onValueChange = {
                                controller.request {
                                    updateNewAccount(newAccount.copy(phoneNumber = it))
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            onClick = {
                                controller.request {
                                    addAccount(newAccount)
                                    onDismissRequest()
                                }
                            }
                        ) {
                            Text(text = "add")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AccountDetailDialog(
        account: UiAccount,
        onDismissRequest: () -> Unit,
        controller: AccountDetailController = viewModel<AccountDetailViewModel>()
    ) {
        val nowAccount by controller.nowAccount.collectAsStateWithLifecycle()
        val newRecord by controller.newAccountRecord.collectAsStateWithLifecycle()
        val recordListState by controller.accountRecordList.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.request {
                updateNowAccount(account)
            }
        }

        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .heightIn(800.dp, 1000.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(5.dp)
            ) {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Card {
                            Column {
                                Text(text = nowAccount.number)
                                Text(text = nowAccount.registerTimestamp)
                            }

                            Spacer(Modifier.size(35.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = nowAccount.name)
                                Text(text = nowAccount.balance, fontSize = 35.sp)
                                Text(text = nowAccount.phoneNumber)
                            }
                        }

                        Component.InputCard(
                            dataList = listOf(newRecord.issuedName, newRecord.difference),
                            labelNameList = listOf("issued name", "difference"),
                            onDataChangedList = listOf(
                                {
                                    controller.request { updateNewAccountRecord(
                                        newRecord.copy(issuedName = it)
                                    ) }
                                },
                                {
                                    controller.request { updateNewAccountRecord(
                                        newRecord.copy(difference = it)
                                    ) }
                                },
                            ),
                            onAddButtonClicked = {
                                controller.request {
                                    addRecord(
                                        accountNumber = nowAccount.number.toInt(),
                                        issuedName = it[0],
                                        difference = it[1].toInt()
                                    )
                                }
                            }
                        )
                    }

                    when(recordListState) {
                        is FlowState.Success -> {
                            val recordList = (recordListState as FlowState.Success<List<UiAccountRecord>>).data
                            LazyColumn {
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
                        is FlowState.Loading -> {
                            Loading()
                        }
                        is FlowState.Failed -> {
                            val throwable = (recordListState as FlowState.Failed<List<UiAccountRecord>>).throwable
                            FailDialog(throwable = throwable) {
                                controller.request {
                                    updateAccountRecordList(nowAccount.number.toInt())
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}