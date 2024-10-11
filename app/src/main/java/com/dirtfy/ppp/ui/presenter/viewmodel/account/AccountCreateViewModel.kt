package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.account.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountCreateViewModel: ViewModel(), AccountCreateController {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountScreen = MutableStateFlow(UiAccountScreen())
    override val uiAccountScreen: StateFlow<UiAccountScreen>
        get() = _uiAccountScreen

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        _uiAccountScreen.update {
            it.copy(newAccount = newAccountData)
        }
    }
    override fun updateNewAccount(newAccountData: UiNewAccount) = request {
        _updateNewAccount(newAccountData)
    }

    private suspend fun _addAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.createAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
//            it.passMap { data ->
//                _uiAccountScreen.update { before ->
//                    before.copy(accountList = before.accountList.passMap { originalList ->
//                        val newList = originalList.toMutableList()
//                        newList.add(data.convertToUiAccount())
//                        _updateNewAccount(UiNewAccount())
//                        newList
//                    })
//                }
//            }
        }
    }
    override fun addAccount(newAccountData: UiNewAccount) = request {
        _addAccount(newAccountData)
    }

    suspend fun _setRandomValidAccountNumberToNewAccount() {
        accountService.createAccountNumber()
            .conflate().collect {
//                it.ignoreMap { value -> //TODO ignore map -- 에러체인 무시해도 되나?
//                    _uiAccountScreen.update { before ->
//                        before.copy(newAccount = before.newAccount.copy(number = value.toString()))
//                    }
//                }
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