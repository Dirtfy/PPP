package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountUpdateController: Controller {
//    val nowAccount: StateFlow<UiAccount>
    val uiAccountScreen: StateFlow<UiAccountScreen>

    suspend fun updateAccount(newAccountData: UiNewAccount)
}