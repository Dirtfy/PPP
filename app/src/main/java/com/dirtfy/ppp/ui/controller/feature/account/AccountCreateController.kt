package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import kotlinx.coroutines.flow.Flow

interface AccountCreateController {
    val screenData: Flow<UiAccountCreateScreenState>

    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()
}