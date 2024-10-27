package com.dirtfy.ppp.ui.presenter.viewmodel.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountCreateViewModel: ViewModel(), AccountCreateController, Tagger {

    private val accountService = AccountBusinessLogic(AccountFireStore())

    private val _screenData = MutableStateFlow(UiAccountCreateScreenState())
    override val screenData: StateFlow<UiAccountCreateScreenState>
        get() = _screenData

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        _screenData.update {
            it.copy(newAccount = newAccountData)
        }
    }
    @Deprecated("screen state synchronized with repository")
    override suspend fun updateNewAccount(newAccountData: UiNewAccount) {
        _updateNewAccount(newAccountData)
    }

    override suspend fun addAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData
        _screenData.update { it.copy(newAccountState = UiScreenState(UiState.LOADING)) }
        accountService.createAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).catch { cause ->
            Log.e(TAG, "addAccount() - createAccount failed \n ${cause.message}")
            _screenData.update {
                it.copy(
                    newAccountState = UiScreenState(UiState.FAIL, cause.message)
                )
            }
        }.collect {
            _screenData.update { before ->
                before.copy(
                    newAccount = UiNewAccount(),
                    newAccountState = UiScreenState(UiState.COMPLETE)
                )
            }
        }
    }

    override suspend fun setRandomValidAccountNumberToNewAccount() {
        _screenData.update {
            it.copy(
                numberGeneratingState = UiScreenState(UiState.LOADING)
            )
        }
        accountService.createAccountNumber()
            .catch { cause ->
                Log.e(TAG, "setRandomValidAccountNumberToNewAccount() - createAccountNumber failed \n ${cause.message}")
                _screenData.update {
                    it.copy(
                        numberGeneratingState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .collect {
                _screenData.update { before ->
                    before.copy(
                        newAccount = before.newAccount.copy(number = it.toString()),
                        numberGeneratingState = UiScreenState(UiState.COMPLETE)
                    )
                }
            }
    }

    override fun request(job: suspend AccountCreateController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}