package com.dirtfy.ppp.contract.view.accounting.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object AccountManagingScreenContract {

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

    interface API {

        @Composable
        fun AccountDetail(
            account: DTO.Account,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordList(
            accountList: List<DTO.Account>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: DTO.Record,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Record(
            record: DTO.Record,
            user: User,
            modifier: Modifier
        )

    }

}