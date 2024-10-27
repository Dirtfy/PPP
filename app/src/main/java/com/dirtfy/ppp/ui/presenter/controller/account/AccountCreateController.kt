package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountCreateController: Controller {

    val uiAccountCreateScreenState: StateFlow<UiAccountCreateScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

    fun request(job: suspend AccountCreateController.() -> Unit)
}