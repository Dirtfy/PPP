package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
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

    override fun updateNewPhoneNumber(newPhoneNumber: String): Pair<String,Int> {
        val cleaned = newPhoneNumber.replace("-", "")
        val areaCodes = arrayOf("031", "032", "033", "041", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064" )

        fun formatNumber(prefix: String, startIndex: Int, middleIndex: Int, endIndex: Int): Pair<String,Int> {
            return when {
                cleaned.length <= startIndex -> Pair(cleaned, 0)
                cleaned.length in (startIndex + 1)..middleIndex -> Pair("$prefix-${cleaned.substring(startIndex)}", 1)
                cleaned.length in (middleIndex + 1)..endIndex -> Pair("$prefix-${cleaned.substring(startIndex, middleIndex)}-${cleaned.substring(middleIndex)}", 2)
                else -> Pair("$prefix-${cleaned.substring(startIndex, startIndex + 4)}-${cleaned.substring(startIndex+4)}", 2)
            }
        }

        return when {
            cleaned.startsWith("02") -> formatNumber("02", 2, 5, 9)
            cleaned.startsWith("010") -> formatNumber("010", 3, 7, 11)
            areaCodes.any { cleaned.startsWith(it) } -> formatNumber(cleaned.substring(0, 3), 3, 6, 10)
            else -> Pair(cleaned, 0)
        }
    }

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        _screenData.update {
            it.copy(newAccount = newAccountData)
        }
    }

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