package com.dirtfy.ppp.contract.view.accounting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

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
            accountList: List<DTO.Account>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Account(
            account: DTO.Account,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun SearchBar(
            searchClue: String,
            user: User,
            modifier: Modifier
        )

    }
}