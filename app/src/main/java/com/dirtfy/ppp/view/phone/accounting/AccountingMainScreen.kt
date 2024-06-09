package com.dirtfy.ppp.view.phone.accounting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.accounting.AccountingScreenContract
import com.dirtfy.ppp.contract.viewmodel.AccountingContract
import com.dirtfy.ppp.view.ui.theme.PPPIcons
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object AccountingMainScreen: AccountingScreenContract.API {
    @Composable
    override fun AccountList(
        accountList: List<AccountingContract.DTO.Account>,
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
        account: AccountingContract.DTO.Account,
        user: User,
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
        user: User,
        modifier: Modifier
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
            onValueChange = { /*TODO*/ },
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
                    )

                    Spacer(Modifier.size(10.dp))

                    val addIcon = PPPIcons.AccountCircle
                    Icon(
                        imageVector = addIcon,
                        contentDescription = addIcon.name
                    )
                }
            },
            modifier = modifier
        )
    }

    @Composable
    override fun Main(
        viewModel: AccountingContract.API,
        user: User,
        modifier: Modifier
    ) {
        val searchClue by viewModel.searchClue.collectAsStateWithLifecycle()
        val accountList by viewModel.accountList.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
        ) {
            SearchBar(
                searchClue = searchClue,
                user = user,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
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
        AccountingContract.DTO.Account(
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
                user = DummyUser,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            AccountingMainScreen.AccountList(
                accountList = accountList,
                user = DummyUser,
                modifier = Modifier
            )
        }
    }
}
