package com.dirtfy.ppp.contract.viewmodel

import com.dirtfy.ppp.contract.viewmodel.user.accounting.managing.AccountManagingUser
import kotlinx.coroutines.flow.StateFlow

object AccountManagingContract {

    object DTO {

        data class Account(
            val number: String,
            val name: String,
            val phoneNumber: String,
            val registerTimestamp: String,
            val balance: String
        )

        data class Record(
            val timestamp: String,
            val userName: String,
            val amount: String,
            val result: String
        )

    }

    interface API: AccountManagingUser {
        val currentAccount: StateFlow<DTO.Account>
        val recordList: StateFlow<List<DTO.Record>>
        val newRecord: StateFlow<DTO.Record>
    }

}