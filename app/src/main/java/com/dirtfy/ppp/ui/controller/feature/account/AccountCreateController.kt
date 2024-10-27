package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount

interface AccountCreateController
    : Controller<UiAccountCreateScreenState, AccountCreateController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

}