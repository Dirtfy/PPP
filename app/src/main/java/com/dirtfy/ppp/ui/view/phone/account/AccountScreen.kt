package com.dirtfy.ppp.ui.view.phone.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.ppp.ui.view.phone.Component
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject

class AccountScreen @Inject constructor(
    val accountController: AccountController,
    val accountCreateScreen: AccountCreateScreen,
    val accountDetailScreen: AccountDetailScreen
){

    @Composable
    fun Main(
        controller: AccountController = accountController
    ) {
        val uiAccountScreen by controller.uiAccountScreenState.collectAsStateWithLifecycle()

        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            controller.updateSearchClue(it.contents?:"")
        }

        LaunchedEffect(key1 = controller) {
            controller.request { updateAccountList() }
        }

        ScreenContent(
            searchClue = uiAccountScreen.searchClue,
            nowAccount = uiAccountScreen.nowAccount,
            accountList = uiAccountScreen.accountList,
            accountListState = uiAccountScreen.accountListState,
            mode = uiAccountScreen.mode,
            onClueChanged = { controller.updateSearchClue(it) },
            onBarcodeIconClick = {
                scanLauncher.launch(
                    ScanOptions().setOrientationLocked(false)
                )
            },
            onAddIconClick = { controller.setMode(UiAccountMode.Create) },
            onItemClick = {
                controller.updateNowAccount(it)
                controller.setMode(UiAccountMode.Detail)
            },
            onRetryClick = {
                controller.request { updateAccountList() }
            },
            onAccountCreate = {
                controller.setMode(UiAccountMode.Main)
                controller.request {
                    updateAccountList()
                }
            },
            onDismissRequest = {
                controller.setMode(UiAccountMode.Main)
            }
        )
    }

    @Composable
    fun ScreenContent(
        searchClue: String,
        nowAccount: UiAccount,
        accountList: List<UiAccount>,
        accountListState: UiScreenState,
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
                accountList = accountList,
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
            placeholder = "account number",
            isNumber = true
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
        accountList: List<UiAccount>,
        accountListState: UiScreenState,
        onItemClick: (UiAccount) -> Unit,
        onRetryClick: () -> Unit
    ) {
        when(accountListState.state) {
            UiState.COMPLETE -> {
                AccountList(
                    accountList = accountList,
                    onItemClick = onItemClick
                )
            }
            UiState.LOADING -> {
                AccountListLoading()
            }
            UiState.FAIL -> {
                AccountListLoadFail(
                    failMessage = accountListState.failMessage,
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            val moreIcon = Icons.Filled.MoreVert
            items(accountList) { account ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onItemClick(account) },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    ListItem(
                        modifier = Modifier
                            .clickable { onItemClick(account) }
                            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
                        overlineContent = { Text(text = "ID : ${account.number}") },
                        headlineContent = { Text(text = account.name) },
                        supportingContent = { Text(text = account.phoneNumber) },
                        trailingContent = {
                            Icon(
                                imageVector = moreIcon,
                                contentDescription = moreIcon.name,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(
                            overlineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // 부제목 색상
                            headlineColor = MaterialTheme.colorScheme.onSurface, // 제목 색상
                            supportingColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) // 보조 텍스트 색상
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun AccountListLoading() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)), // 배경 색상을 좀 더 부드럽게
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp), // 크기를 조정
                color = MaterialTheme.colorScheme.primary, // 색상 조정
                strokeWidth = 8.dp // 두께 조정
            )
        }
    }

    @Composable
    fun AccountListLoadFail(
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
    
    @Composable
    fun AccountCreateDialog(
        onAccountCreate: (UiNewAccount) -> Unit,
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            accountCreateScreen.Main(
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
            accountDetailScreen.Main(account = account)
        }
    }
}