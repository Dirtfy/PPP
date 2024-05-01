package com.dirtfy.ppp.accounting.accounting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.model.AccountRepository
import com.dirtfy.ppp.accounting.accounting.view.AccountTestViewData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel() {

    private val _viewState = MutableStateFlow(AccountTestViewData())
    val viewState: StateFlow<AccountTestViewData> = _viewState.asStateFlow()

    fun createAccount(data: AccountData) {
        viewModelScope.launch {
            AccountRepository.create(data)
        }
    }
}