package com.dirtfy.ppp.ui.presenter.viewmodel.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountUpdateController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountUpdateViewModel: ViewModel(), AccountUpdateController, Tagger {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountUpdateScreenState = MutableStateFlow(UiAccountUpdateScreenState())
    override val uiAccountUpdateScreenState: StateFlow<UiAccountUpdateScreenState>
        get() = _uiAccountUpdateScreenState

    override suspend fun updateAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.updateAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).catch { cause ->
            Log.e(TAG, "updateAccount() - updateAccount failed \n ${cause.message}")
            _uiAccountUpdateScreenState.update {
                it.copy(
                    updateAccountState = UiScreenState(UiState.FAIL, cause.message)
                )
            }
        }.collect { }
    }

    override fun request(job: suspend AccountUpdateController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }

}