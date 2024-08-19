package com.dirtfy.ppp.contract.viewmodel.accounting

import androidx.compose.runtime.State

object AccountingViewModelContract {

    object DTO {
        data class Account(
            val number: String,
            val name: String,
            val registerDate: String
        )
    }

    object SearchBar {
        interface API {
            val searchClue: State<String>

            fun clueChanged(now: String)
            fun searchByClue()
            fun addAccount(account: DTO.Account, phoneNumber: String)
        }
    }

    object AccountList {
        interface API {
            val accountList: State<List<DTO.Account>>

            fun checkAccountList()

            fun buildAccountArgumentString(data: DTO.Account): String
        }
    }

    interface API: SearchBar.API, AccountList.API {
        val isCreatingAccount: State<Boolean>

        fun setIsCreatingAccount(value: Boolean)
    }
}