package com.dirtfy.ppp.view.phone.accounting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.common.DummyAccountingViewModel
import com.dirtfy.ppp.common.DummyHomeViewModel
import com.dirtfy.ppp.contract.view.accounting.AccountingViewContract
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

object AccountingMainScreen: AccountingViewContract.API {
    @Composable
    override fun AccountList(
        accountList: List<AccountingViewModelContract.DTO.Account>,
        viewModel: AccountingViewModelContract.AccountList.API,
        homeViewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(accountList) {
                Account(
                    account = it,
                    viewModel = viewModel,
                    homeViewModel = homeViewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun Account(
        account: AccountingViewModelContract.DTO.Account,
        viewModel: AccountingViewModelContract.AccountList.API,
        homeViewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier.clickable {
                homeViewModel.navigateTo(
                    HomeViewModelContract.DTO.Screen.AccountManaging,
                    viewModel.buildAccountArgumentString(account)
                )
            }
        ) {
            ListItem(
                overlineContent = {
                    Text(text = account.number)
                },
                headlineContent = {
                    Text(text = account.name)
                },
                supportingContent = {
                    Text(text = account.registerDate)
                },
                trailingContent = {
                    Icon(
                        imageVector = PPPIcons.MoreVert,
                        contentDescription = PPPIcons.MoreVert.name
                    )
                }
            )
        }
    }

    @Composable
    override fun SearchBar(
        searchClue: String,
        viewModel: AccountingViewModelContract.API,
        modifier: Modifier
    ) {
        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            viewModel.clueChanged(it.contents)
            viewModel.searchByClue()
        }

        TextField(
            leadingIcon = {
                val searchIcon = PPPIcons.Search
                Icon(
                    imageVector = searchIcon,
                    contentDescription = searchIcon.name
                )
            },
            label = { Text(text = "account number") },
            value = searchClue,
            onValueChange = {
                viewModel.clueChanged(it)
                viewModel.searchByClue()
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    val barCodeIcon = PPPIcons.Menu
                    Icon(
                        imageVector = barCodeIcon,
                        contentDescription = barCodeIcon.name,
                        modifier = Modifier
                            .rotate(90f)
                            .clickable {
                                scanLauncher.launch(ScanOptions())
                            }
                    )

                    Spacer(Modifier.size(10.dp))

                    val addIcon = PPPIcons.AccountCircle
                    Icon(
                        imageVector = addIcon,
                        contentDescription = addIcon.name,
                        modifier = Modifier
                            .clickable {
                                viewModel.setIsCreatingAccount(true)
                            }
                    )
                }
            },
            modifier = modifier
        )
    }

    @Composable
    override fun NewAccountDialog(
        viewModel: AccountingViewModelContract.API
    ) {
        var newAccount by remember {
            mutableStateOf(
                AccountingViewModelContract
                    .DTO.Account("","","")
            )
        }
        var phoneNumber by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = {
//                viewModel.checkAccountList()
                viewModel.setIsCreatingAccount(false)
            }
        ) {
            Surface(
                modifier = Modifier.wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = newAccount.number,
                        onValueChange = {
                            newAccount = newAccount.copy(number = it)
                        },
                        placeholder = { Text(text = "number") }
                    )
                    TextField(
                        value = newAccount.name,
                        onValueChange = {
                            newAccount = newAccount.copy(name = it)
                        },
                        placeholder = { Text(text = "name") }
                    )
                    TextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                        },
                        placeholder = { Text(text = "phone number") }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val closeIcon = PPPIcons.Close
                        Icon(
                            imageVector = closeIcon,
                            contentDescription = closeIcon.name,
                            modifier = Modifier.weight(1f)
                        )

                        val addIcon = PPPIcons.Add
                        Icon(
                            imageVector = addIcon,
                            contentDescription = addIcon.name,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.addAccount(
                                        newAccount,
                                        phoneNumber
                                    )
                                    viewModel.setIsCreatingAccount(false)
                                }
                        )
                    }

                }
            }

        }
    }

    @Composable
    override fun Main(
        viewModel: AccountingViewModelContract.API,
        homeViewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        val searchClue by viewModel.searchClue
        val accountList by viewModel.accountList
        val isCreatingAccount by viewModel.isCreatingAccount

        LaunchedEffect(key1 = viewModel) {
            viewModel.checkAccountList()
        }

        Column(
            modifier = modifier
        ) {
            SearchBar(
                searchClue = searchClue,
                viewModel = viewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            AccountList(
                accountList = accountList,
                viewModel = viewModel,
                homeViewModel = homeViewModel,
                modifier = Modifier
            )
        }

        if (isCreatingAccount) {
            NewAccountDialog(viewModel = viewModel)
        }
    }
}


@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun AccountingMainScreenPreview() {
    val accountList = MutableList(10) {
        AccountingViewModelContract.DTO.Account(
            number = "$it",
            name = "test_$it",
            registerDate = "2024.6.${it+1}"
        )
    }
    val searchClue = ""

    PPPTheme {
        Column(
            modifier = Modifier
        ) {
            AccountingMainScreen.SearchBar(
                searchClue = searchClue,
                viewModel = DummyAccountingViewModel,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            AccountingMainScreen.AccountList(
                accountList = accountList,
                viewModel = DummyAccountingViewModel,
                homeViewModel = DummyHomeViewModel,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewAccountDialogPreview() {
    PPPTheme {
        AccountingMainScreen.NewAccountDialog(
            viewModel = DummyAccountingViewModel
        )
    }
}
