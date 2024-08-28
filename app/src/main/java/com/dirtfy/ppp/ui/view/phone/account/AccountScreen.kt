package com.dirtfy.ppp.ui.view.phone.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountMode
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
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

        val mode by controller.mode.collectAsStateWithLifecycle()

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

        ScreenContent(
            searchClue = searchClue,
            nowAccount = nowAccount,
            accountListState = accountListState,
            mode = mode,
            onClueChanged = { controller.request { updateSearchClue(it) } },
            onBarcodeIconClick = {
                scanLauncher.launch(
                    ScanOptions().setOrientationLocked(false)
                )
            },
            onAddIconClick = { controller.request { setMode(UiAccountMode.Create) } },
            onItemClick = {
                controller.request {
                    updateNowAccount(it)
                    setMode(UiAccountMode.Detail)
                }
            },
            onRetryClick = {
                controller.request { updateAccountList() }
            },
            onAccountCreate = {
                controller.request {
                    setMode(UiAccountMode.Main)
                    updateAccountList()
                }
            },
            onDismissRequest = {
                controller.request { setMode(UiAccountMode.Main) }
            }
        )
    }

    @Composable
    fun ScreenContent(
        searchClue: String,
        nowAccount: UiAccount,
        accountListState: FlowState<List<UiAccount>>,
        mode: UiAccountMode,
        onClueChanged: (String) -> Unit,
        onBarcodeIconClick: () -> Unit,
        onAddIconClick: () -> Unit,
        onItemClick: (UiAccount) -> Unit,
        onRetryClick: () -> Unit,
        onAccountCreate: (UiNewAccount) -> Unit,
        onDismissRequest: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                searchClue = searchClue,
                onClueChanged = onClueChanged,
                onBarcodeIconClick = onBarcodeIconClick,
                onAddIconClick = onAddIconClick
            )
            Spacer(modifier = Modifier.size(10.dp))

            AccountListState(
                accountListState = accountListState,
                onItemClick = onItemClick,
                onRetryClick = onRetryClick
            )

            when(mode) {
                UiAccountMode.Main -> {

                }
                UiAccountMode.Create -> {
                    AccountCreateDialog(
                        onAccountCreate = onAccountCreate,
                        onDismissRequest = onDismissRequest
                    )
                }
                UiAccountMode.Detail -> {
                    AccountDetailDialog(
                        account = nowAccount,
                        onDismissRequest = onDismissRequest
                    )
                }
                UiAccountMode.Update -> {

                }
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
        Component.SearchBar(
            searchClue = searchClue,
            onClueChanged = onClueChanged,
            placeholder = "account number"
        ) {
            BarcodeIcon(onClick = onBarcodeIconClick)
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

    @Composable
    fun BarcodeIcon(
        onClick: () -> Unit
    ) {
        val barcodeIcon = Icons.Filled.Menu
        Icon(
            imageVector = barcodeIcon, contentDescription = barcodeIcon.name,
            modifier = Modifier
                .rotate(90f)
                .clickable {
                    onClick()
                }
        )
    }

    @Composable
    fun AccountListState(
        accountListState: FlowState<List<UiAccount>>,
        onItemClick: (UiAccount) -> Unit,
        onRetryClick: () -> Unit
    ) {
        when(accountListState) {
            is FlowState.Success -> {
                val accountList = accountListState.data
                AccountList(
                    accountList = accountList,
                    onItemClick = onItemClick
                )
            }
            is FlowState.Loading -> {
                AccountListLoading()
            }
            is FlowState.Failed -> {
                val throwable = accountListState.throwable
                AccountListLoadFail(
                    throwable = throwable,
                    onRetryClick = onRetryClick
                )
            }
        }
    }

    @Composable
    fun AccountList(
        accountList: List<UiAccount>,
        onItemClick: (UiAccount) -> Unit
    ) {
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
                        onItemClick(it)
                    }
                )
            }
        }
    }

    @Composable
    fun AccountListLoading() {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun AccountListLoadFail(
        throwable: Throwable,
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
            title = { Text(text = throwable.message?: "unknown error") }
        )
    }
    
    @Composable
    fun AccountCreateDialog(
        onAccountCreate: (UiNewAccount) -> Unit,
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            AccountCreateScreen.Main(
                onAccountCreate = onAccountCreate
            )
        }
    }

    @Composable
    fun AccountDetailDialog(
        account: UiAccount,
        onDismissRequest: () -> Unit
    ) { //TODO maxHeight 설정?
        Dialog(onDismissRequest = onDismissRequest) {
            AccountDetailScreen.Main(account = account)
        }
    }
}