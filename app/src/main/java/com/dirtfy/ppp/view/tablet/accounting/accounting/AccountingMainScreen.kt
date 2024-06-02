package com.dirtfy.ppp.view.tablet.accounting.accounting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dirtfy.ppp.contract.DummyUser
import com.dirtfy.ppp.contract.User
import com.dirtfy.ppp.contract.view.accounting.AccountingScreenContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object AccountingMainScreen: AccountingScreenContract.API {
    @Composable
    override fun AccountList(
        accountList: List<AccountingScreenContract.DTO.Account>,
        user: User,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(accountList) {
                Account(
                    account = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun Account(
        account: AccountingScreenContract.DTO.Account,
        user: User,
        modifier: Modifier
    ) {
        ListItem(
            leadingContent = {
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
            },
            modifier = modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun SearchBar(
        searchClue: String,
        user: User,
        modifier: Modifier
    ) {
//        androidx.compose.material3.SearchBar(
//            query = searchClue,
//            onQueryChange = {
//
//            },
//            onSearch = {
//
//            },
//            active = true,
//            onActiveChange = {
//
//            }
//        ) {
//
//        }
    }

    @Composable
    fun Main(
        searchClue: String,
        accountList: List<AccountingScreenContract.DTO.Account>,
        user: User,
        modifier: Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            SearchBar(
                searchClue = searchClue,
                user = user,
                modifier = Modifier
            )
            AccountList(
                accountList = accountList,
                user = user,
                modifier = Modifier
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountingMainScreenPreview() {
    val accountList = MutableList(10) {
        AccountingScreenContract.DTO.Account(
            number = "$it",
            name = "test_$it",
            registerDate = "2024.6.${it+1}"
        )
    }

    PPPTheme {
        AccountingMainScreen.Main(
            searchClue = "",
            accountList = accountList,
            user = DummyUser,
            modifier = Modifier
        )
    }
}
