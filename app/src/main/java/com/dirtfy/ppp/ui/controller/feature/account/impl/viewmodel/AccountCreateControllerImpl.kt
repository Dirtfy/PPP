package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.PhoneNumberFormatConverter.formatPhoneNumber
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountCreateControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountCreateController, Tagger {

    private val _screenData = MutableStateFlow(UiAccountCreateScreenState())
    override val screenData: Flow<UiAccountCreateScreenState>
        get() = _screenData

    override fun updateNewAccount(newAccountData: UiNewAccount) {
        _screenData.update {
            it.copy(newAccount = newAccountData)
        }
    }

    override suspend fun addAccount(onComplete: () -> Unit) {
        val (number, name, phoneNumber) = _screenData.value.newAccount
        _screenData.update { it.copy(addAccountState = UiScreenState(UiState.LOADING)) }
        accountBusinessLogic.createAccount(
            numberString = number,
            name = name,
            phoneNumber = formatPhoneNumber(phoneNumber)
        ).catch { cause ->
            Log.e(TAG, "addAccount() - createAccount failed \n ${cause.message}")
            _screenData.update {
                it.copy(
                    addAccountState = UiScreenState(UiState.FAIL, cause)
                )
            }
        }.collect {
            _screenData.update { before ->
                before.copy(
                    newAccount = UiNewAccount(),
                    addAccountState = UiScreenState(UiState.COMPLETE)
                )
            }
            onComplete()
        }
    }

    override suspend fun setRandomValidAccountNumberToNewAccount() {
        _screenData.update {
            it.copy(
                numberGeneratingState = UiScreenState(UiState.LOADING)
            )
        }
        accountBusinessLogic.createAccountNumber()
            .catch { cause ->
                Log.e(TAG, "setRandomValidAccountNumberToNewAccount() - createAccountNumber failed \n ${cause.message}")
                _screenData.update {
                    it.copy(
                        numberGeneratingState = UiScreenState(UiState.FAIL, cause)
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

    override fun setAddAccountState(state: UiScreenState){
        _screenData.update {
            it.copy(addAccountState = state)
        }
    }

    override fun setNumberGeneratingState(state: UiScreenState){
        _screenData.update{
            it.copy(numberGeneratingState = state)
        }
    }
}