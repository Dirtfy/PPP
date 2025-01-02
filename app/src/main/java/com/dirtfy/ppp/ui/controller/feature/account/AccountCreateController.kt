package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import kotlinx.coroutines.flow.Flow

interface AccountCreateController {
    val screenData: Flow<UiAccountCreateScreenState>

    fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(onComplete: () -> Unit)
    suspend fun setRandomValidAccountNumberToNewAccount()
    fun setAddAccountState(state: UiScreenState)
    fun setNumberGeneratingState(state: UiScreenState)
}