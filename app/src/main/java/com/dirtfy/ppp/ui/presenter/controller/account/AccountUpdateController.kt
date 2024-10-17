package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountUpdateController: Controller {
//    val nowAccount: StateFlow<UiAccount>
    val uiAccountUpdateScreenState: StateFlow<UiAccountUpdateScreenState>

    suspend fun updateAccount(newAccountData: UiNewAccount)

    fun request(job: suspend AccountUpdateController.() -> Unit)
}