package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountCreateController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountCreateViewModel @Inject constructor(
    private val accountService: AccountService
): ViewModel(), AccountCreateController {

    private val bubbles = Bubbles()

    override val newAccount: StateFlow<UiNewAccount>
        get() = bubbles.newAccount.get()

    private fun _updateNewAccount(newAccountData: UiNewAccount) {
        bubbles.newAccount.set(newAccountData)
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
            bubbles.accountList.let { bubble -> // TODO newAccount로 바꾸기
                bubble.set(it.passMap { data -> // TODO FlowState 반영되도록
                    val newList = bubble.value.toMutableList()
                    newList.add(data.convertToUiAccount())
                    newList
                })
            }
        }
    }
    override fun addAccount(newAccountData: UiNewAccount) = request {
        _addAccount(newAccountData)
    }

    suspend fun _setRandomValidAccountNumberToNewAccount() {
        accountService.createAccountNumber()
            .conflate().collect {
                bubbles.newAccount.let { newAccount ->
                    newAccount.set(
                        it.ignoreMap { value ->
                            newAccount.value.copy(number = value.toString())
                        }?: UiNewAccount()
                    )
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