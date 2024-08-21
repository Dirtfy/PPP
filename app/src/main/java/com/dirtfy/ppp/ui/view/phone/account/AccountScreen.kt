package com.dirtfy.ppp.ui.view.phone.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccount
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerNewAccount
import com.dirtfy.ppp.ui.presenter.viewmodel.AccountViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

object AccountScreen {

    @Composable
    fun Main(
        controller: AccountController = viewModel<AccountViewModel>()
    ) {
        val searchClue by controller.searchClue.collectAsStateWithLifecycle()
        val accountListState by controller.accountList.collectAsStateWithLifecycle()
        val newAccount by controller.newAccount.collectAsStateWithLifecycle()

        val isAccountCreateMode by controller.isAccountCreateMode.collectAsStateWithLifecycle()

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
                    val accountList = (accountListState as FlowState.Success<List<ControllerAccount>>).data
                    LazyColumn {
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
                    val throwable = (accountListState as FlowState.Failed<List<ControllerAccount>>).throwable

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = { controller.request { updateAccountList() } }) {
                                Text(text = "OK")
                            }
                        },
                        title = { Text(text = throwable.message?: "unknown error") }
                    )
                }
            }
            
            if (isAccountCreateMode) {
                AccountCreateDialog(
                    newAccount = newAccount,
                    onNewAccountChanged = { controller.request { updateNewAccount(it) } },
                    onAccountNumberRandomIconClick = {
                        controller.request { setRandomValidAccountNumberToNewAccount() }
                    },
                    onAddButtonClick = { controller.request { addAccount(it) } },
                    onDismissRequest = {
                        controller.request {
                            setAccountCreateMode(false)
                            updateNewAccount(ControllerNewAccount())
                        }
                    }
                )
            }
        }

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
        newAccount: ControllerNewAccount,
        onNewAccountChanged: (ControllerNewAccount) -> Unit,
        onAccountNumberRandomIconClick: () -> Unit,
        onAddButtonClick: (ControllerNewAccount) -> Unit,
        onDismissRequest: () -> Unit
    ) {
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
                                onNewAccountChanged(newAccount.copy(number = it))
                            },
                            trailingIcon = {
                                val randomIcon = Icons.Filled.Refresh
                                Icon(
                                    imageVector = randomIcon,
                                    contentDescription = randomIcon.name,
                                    modifier = Modifier.clickable {
                                        onAccountNumberRandomIconClick()
                                    }
                                )
                            },
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        TextField(
                            label = { Text(text = "account name") },
                            value = newAccount.name,
                            onValueChange = {
                                onNewAccountChanged(newAccount.copy(name = it))
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        TextField(
                            label = { Text(text = "phone number") },
                            value = newAccount.phoneNumber,
                            onValueChange = {
                                onNewAccountChanged(newAccount.copy(phoneNumber = it))
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            onClick = {
                                onAddButtonClick(newAccount)
                                onDismissRequest()
                            }
                        ) {
                            Text(text = "add")
                        }
                    }
                }
            }
        }
    }
}