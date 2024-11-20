package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.feature.account.AccountUpdateController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountUpdateControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountUpdateController, Tagger {

    private val _screenData = MutableStateFlow(UiAccountUpdateScreenState())
    override val screenData: Flow<UiAccountUpdateScreenState>
        get() = _screenData

    override suspend fun updateAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        _screenData.update { it.copy(updateAccountState = UiScreenState(UiState.LOADING)) }
        accountBusinessLogic.updateAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).catch { cause ->
            Log.e(TAG, "updateAccount() - updateAccount failed \n ${cause.message}")
            _screenData.update {
                it.copy(
                    updateAccountState = UiScreenState(UiState.FAIL, cause)
                )
            }
        }.collect {
            _screenData.update { it.copy(updateAccountState = UiScreenState(UiState.COMPLETE)) }
        }
    }

}