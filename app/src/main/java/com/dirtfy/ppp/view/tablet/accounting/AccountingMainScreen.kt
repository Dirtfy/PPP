package com.dirtfy.ppp.view.tablet.accounting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.common.DummyAccountingViewModel
import com.dirtfy.ppp.common.DummyHomeViewModel
import com.dirtfy.ppp.contract.view.accounting.AccountingViewContract
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object AccountingMainScreen: AccountingViewContract.API {

    @Composable
    override fun AccountList(
        accountList: List<AccountingViewModelContract.DTO.Account>,
        viewModel: AccountingViewModelContract.AccountList.API,
        homeViewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
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
                /* TODO */
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

        Column(
            modifier = modifier
        ) {
            TextField(
                leadingIcon = {
                    val searchIcon = PPPIcons.Search
                    Icon(
                        imageVector = searchIcon,
                        contentDescription = searchIcon.name
                    )
                },
                value = searchClue,
                onValueChange = { /*TODO*/ }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.widthIn(100.dp, 300.dp)
            ) {
                ListItem(
                    headlineContent = { Text(text = "Scan Barcode") },
                    trailingContent = {
                        val barCodeIcon = PPPIcons.Menu
                        Icon(
                            imageVector = barCodeIcon,
                            contentDescription = barCodeIcon.name,
                            modifier = Modifier
                                .rotate(90f)
                        )
                    }
                )

                ListItem(
                    headlineContent = { Text(text = "Add Account") },
                    trailingContent = {
                        val addIcon = PPPIcons.AccountCircle
                        Icon(
                            imageVector = addIcon,
                            contentDescription = addIcon.name
                        )
                    }
                )
            }
        }


    }

    @Composable
    override fun NewAccountDialog(viewModel: AccountingViewModelContract.API) {
        TODO("Not yet implemented")
    }

    @Composable
    override fun Main(
        viewModel: AccountingViewModelContract.API,
        homeViewModel: HomeViewModelContract.NavGraph.API,
        modifier: Modifier
    ) {
        val accountList by viewModel.accountList
        val searchClue by viewModel.searchClue

        Row(
            modifier = modifier
        ) {
            AccountList(
                accountList = accountList,
                viewModel = viewModel,
                homeViewModel = homeViewModel,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )
            
            Spacer(Modifier.size(10.dp))

            SearchBar(
                searchClue = searchClue,
                viewModel = viewModel,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.TABLET)
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
        Row(
            modifier = Modifier
        ) {
            AccountingMainScreen.AccountList(
                accountList = accountList,
                viewModel = DummyAccountingViewModel,
                homeViewModel = DummyHomeViewModel,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            Spacer(Modifier.size(10.dp))

            AccountingMainScreen.SearchBar(
                searchClue = searchClue,
                viewModel = DummyAccountingViewModel,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )
        }
    }
}
