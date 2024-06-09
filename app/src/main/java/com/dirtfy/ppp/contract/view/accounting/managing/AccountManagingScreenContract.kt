package com.dirtfy.ppp.contract.view.accounting.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.AccountManagingContract

object AccountManagingScreenContract {

    interface API {

        @Composable
        fun AccountDetail(
            account: AccountManagingContract.DTO.Account,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordList(
            recordList: List<AccountManagingContract.DTO.Record>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: AccountManagingContract.DTO.Record,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Record(
            record: AccountManagingContract.DTO.Record,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: AccountManagingContract.API,
            user: User,
            modifier: Modifier
        )

    }

}