package com.dirtfy.ppp.ui.view.phone.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.account.AccountController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode
import com.dirtfy.ppp.ui.view.Component
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject

class AccountScreen @Inject constructor(
    private val accountController: AccountController,
    private val accountCreateScreen: AccountCreateScreen,
    private val accountDetailScreen: AccountDetailScreen
){

    @Composable
    fun Main(
        controller: AccountController = accountController
    ) {
        val uiAccountScreen by controller.screenData.collectAsStateWithLifecycle()

        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            controller.updateSearchClue(it.contents?:"")
        }

        ScreenContent(
            searchClue = uiAccountScreen.searchClue,
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
                controller.retryUpdateAccountList()
            },
            onDismissRequest = {
                controller.setMode(UiAccountMode.Main)
            },
            onDismissHandleDialogRequest = {
                controller.setAccountListState(UiScreenState(UiState.COMPLETE))
            }
        )
    }

    @Composable
    fun ScreenContent(
        searchClue: String,
        accountList: List<UiAccount>,
        accountListState: UiScreenState,
        mode: UiAccountMode,
        onClueChanged: (String) -> Unit,
        onBarcodeIconClick: () -> Unit,
        onAddIconClick: () -> Unit,
        onItemClick: (UiAccount) -> Unit,
        onRetryClick: () -> Unit,
        onDismissRequest: () -> Unit,
        onDismissHandleDialogRequest: () -> Unit
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

            Component.HandleUiStateDialog(
                uiState = accountListState,
                onDismissRequest = onDismissHandleDialogRequest, onRetryAction = onRetryClick,
                onComplete = {
                    AccountList(
                        accountList = accountList,
                        onItemClick = onItemClick)
                }
            )

            when(mode) {
                UiAccountMode.Main -> {
                }
                UiAccountMode.Create -> {
                    AccountCreateDialog(
                        onDismissRequest = onDismissRequest
                    )
                }
                UiAccountMode.Detail -> {
                    AccountDetailDialog(
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
            placeholder = stringResource(R.string.account_name),
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
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ),
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
    fun AccountCreateDialog(
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            accountCreateScreen.Main()
        }
    }

    @Composable
    fun AccountDetailDialog(
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            accountDetailScreen.Main()
        }
    }
}