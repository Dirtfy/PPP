package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel(), AccountController {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountScreen = MutableStateFlow(UiAccountScreen())
    override val uiAccountScreen: StateFlow<UiAccountScreen>
        get() = _uiAccountScreen


    override suspend fun updateAccountList() {
        accountService.readAllAccounts().conflate().collect {
            _uiAccountScreen.update { before ->
                before.copy(accountList = it.passMap { data ->
                    data.map { account -> account.convertToUiAccount() }
                })
            }
        }
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        _uiAccountScreen.update {
            it.copy(nowAccount = account)
        }
    }

    override suspend fun updateSearchClue(clue: String) {
        _uiAccountScreen.update {
            it.copy(searchClue = clue)
        }
        accountService.readAllAccounts()
            .conflate().collect {
                _uiAccountScreen.update { before ->
                    before.copy(accountList = it.passMap { data ->
                        val filter = data.map { account -> account.number.toString() }
                            .filter { number -> number.contains(clue) }

                        data.map { account -> account.convertToUiAccount() }
                            .filter { account -> filter.contains(account.number) }
                    })
                }
            }
    }

    override suspend fun setMode(mode: UiAccountMode) {
        _uiAccountScreen.update {
            it.copy(mode = mode)
        }
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}