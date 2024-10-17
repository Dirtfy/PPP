package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountCreateViewModel: ViewModel(), AccountCreateController, Tagger {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountCreateScreenState = MutableStateFlow(UiAccountCreateScreenState())
    override val uiAccountCreateScreenState: StateFlow<UiAccountCreateScreenState>
        get() = _uiAccountCreateScreenState

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        _uiAccountCreateScreenState.update {
            it.copy(newAccount = newAccountData)
        }
    }
    @Deprecated("screen state synchronized with repository")
    override fun updateNewAccount(newAccountData: UiNewAccount) = request {
        _updateNewAccount(newAccountData)
    }

    private suspend fun _addAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData
        accountService.createAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).collect {
            _uiAccountCreateScreenState.update { before ->
                before.copy(newAccount = UiNewAccount())
            }
        }
    }
    override fun addAccount(newAccountData: UiNewAccount) = request {
        _addAccount(newAccountData)
    }

    suspend fun _setRandomValidAccountNumberToNewAccount() {
        accountService.createAccountNumber()
            .conflate().collect {
                _uiAccountCreateScreenState.update { before ->
                    before.copy(newAccount = before.newAccount.copy(number = it.toString()))
                }
            }
    }
    override fun setRandomValidAccountNumberToNewAccount() = request {
        _setRandomValidAccountNumberToNewAccount()
    }

    override fun request(job: suspend AccountCreateController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}