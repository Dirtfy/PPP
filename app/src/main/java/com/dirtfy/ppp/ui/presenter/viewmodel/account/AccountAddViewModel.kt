package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountAddController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class AccountAddViewModel: ViewModel(), AccountAddController {

    private val accountService = AccountService(AccountFireStore())

    private val bubbles = Bubbles()

    override val newAccount: StateFlow<UiNewAccount>
        get() = bubbles.newAccount.get()

    override suspend fun updateNewAccount(newAccountData: UiNewAccount) {
        bubbles.newAccount.set(newAccountData)
    }

    override suspend fun addAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.createAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
            bubbles.accountList.let { bubble ->
                bubble.set(it.passMap { data ->
                    val newList = bubble.value.toMutableList()
                    newList.add(data.convertToUiAccount())
                    newList
                })
            }
        }
    }

    override suspend fun setRandomValidAccountNumberToNewAccount() {
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

    override fun request(job: suspend AccountAddController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}