package com.dirtfy.ppp.contract.viewmodel

import com.dirtfy.ppp.contract.user.accounting.AccountingUser
import kotlinx.coroutines.flow.StateFlow

object AccountingContract {

    object DTO {

        data class Account(
            val number: String,
            val name: String,
            val registerDate: String
        )
    }

    interface API: AccountingUser {
        val accountList: StateFlow<List<DTO.Account>>
        val searchClue: StateFlow<String>
    }
}