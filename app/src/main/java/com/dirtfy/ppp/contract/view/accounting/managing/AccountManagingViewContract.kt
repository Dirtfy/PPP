package com.dirtfy.ppp.contract.view.accounting.managing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract

object AccountManagingViewContract {

    interface API {

        @Composable
        fun AccountDetail(
            account: AccountManagingViewModelContract.DTO.Account,
            viewModel: AccountManagingViewModelContract.AccountDetail.API,
            modifier: Modifier
        )

        @Composable
        fun RecordList(
            recordList: List<AccountManagingViewModelContract.DTO.Record>,
            viewModel: AccountManagingViewModelContract.RecordList.API,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: AccountManagingViewModelContract.DTO.Record,
            viewModel: AccountManagingViewModelContract.RecordList.API,
            modifier: Modifier
        )

        @Composable
        fun NewRecord(
            record: AccountManagingViewModelContract.DTO.Record,
            viewModel: AccountManagingViewModelContract.NewRecord.API,
            modifier: Modifier
        )

        @Composable
        fun Main(
            startAccountDetail: AccountManagingViewModelContract.DTO.Account,
            viewModel: AccountManagingViewModelContract.API,
            modifier: Modifier
        )
    }

}