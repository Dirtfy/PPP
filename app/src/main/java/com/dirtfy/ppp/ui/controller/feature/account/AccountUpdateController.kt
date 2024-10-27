package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount

interface AccountUpdateController
    : Controller<UiAccountUpdateScreenState, AccountUpdateController> {

    suspend fun updateAccount(newAccountData: UiNewAccount)

}