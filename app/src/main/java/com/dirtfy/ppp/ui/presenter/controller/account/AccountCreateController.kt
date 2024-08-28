package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountCreateController: Controller {
    val newAccount: StateFlow<UiNewAccount>

    fun updateNewAccount(newAccountData: UiNewAccount)
    fun addAccount(newAccountData: UiNewAccount)
    fun setRandomValidAccountNumberToNewAccount()

    fun request(job: suspend AccountCreateController.() -> Unit)
}