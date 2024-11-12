package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.feature.account.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import kotlinx.coroutines.flow.Flow

interface AccountUpdateController {
    val screenData: Flow<UiAccountUpdateScreenState>

    suspend fun updateAccount(newAccountData: UiNewAccount)

}