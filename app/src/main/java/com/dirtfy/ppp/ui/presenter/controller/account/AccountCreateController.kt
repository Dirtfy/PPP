package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller

interface AccountCreateController
    : Controller<UiAccountCreateScreenState, AccountCreateController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

}