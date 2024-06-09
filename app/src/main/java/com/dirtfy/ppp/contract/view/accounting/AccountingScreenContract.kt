package com.dirtfy.ppp.contract.view.accounting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.AccountingContract

object AccountingScreenContract {

    object DTO {

        data class Account(
            val number: String,
            val name: String,
            val registerDate: String
        )

    }

    interface API {

        @Composable
        fun AccountList(
            accountList: List<AccountingContract.DTO.Account>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Account(
            account: AccountingContract.DTO.Account,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun SearchBar(
            searchClue: String,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: AccountingContract.API,
            user: User,
            modifier: Modifier
        )

    }
}