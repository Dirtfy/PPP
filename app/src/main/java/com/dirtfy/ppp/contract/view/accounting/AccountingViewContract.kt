package com.dirtfy.ppp.contract.view.accounting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.HomeViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract

object AccountingViewContract {

    interface API {

        @Composable
        fun AccountList(
            accountList: List<AccountingViewModelContract.DTO.Account>,
            viewModel: AccountingViewModelContract.AccountList.API,
            homeViewModel: HomeViewModelContract.NavGraph.API,
            modifier: Modifier
        )

        @Composable
        fun Account(
            account: AccountingViewModelContract.DTO.Account,
            viewModel: AccountingViewModelContract.AccountList.API,
            homeViewModel: HomeViewModelContract.NavGraph.API,
            modifier: Modifier
        )

        @Composable
        fun SearchBar(
            searchClue: String,
            viewModel: AccountingViewModelContract.API,
            modifier: Modifier
        )

        @Composable
        fun NewAccountDialog(
            viewModel: AccountingViewModelContract.API
        )

        @Composable
        fun Main(
            viewModel: AccountingViewModelContract.API,
            homeViewModel: HomeViewModelContract.NavGraph.API,
            modifier: Modifier
        )

    }
}