package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountCreateViewModel: ViewModel(), AccountCreateController {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountCreateScreenState = MutableStateFlow(UiAccountCreateScreenState())
    override val uiAccountCreateScreenState: StateFlow<UiAccountCreateScreenState>
        get() = _uiAccountCreateScreenState

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        _uiAccountCreateScreenState.update {
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
            // TODO 이거 해도 create 이후 text field 값이 유지됨.
            _uiAccountCreateScreenState.update { it.copy(newAccount = UiNewAccount()) }
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
                // TODO 왜 버튼 눌러도 text field에 값이 안 들어 오냐
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