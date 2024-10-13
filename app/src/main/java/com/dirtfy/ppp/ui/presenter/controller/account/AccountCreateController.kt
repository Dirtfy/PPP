package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountCreateController: Controller {
//    val newAccount: StateFlow<UiNewAccount>
    val uiAccountCreateScreenState: StateFlow<UiAccountCreateScreenState>

    fun updateNewAccount(newAccountData: UiNewAccount)
    fun addAccount(newAccountData: UiNewAccount)
    fun setRandomValidAccountNumberToNewAccount()

    fun request(job: suspend AccountCreateController.() -> Unit)
}