package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

interface AccountCreateController {
    val screenData: Flow<UiAccountCreateScreenState>

    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount, onComplete: (Boolean) -> Unit)
    suspend fun setRandomValidAccountNumberToNewAccount()
    fun setNewAccountState(state: UiScreenState)
    fun setNumberGeneratingState(state: UiScreenState)
}