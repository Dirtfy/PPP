package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountUpdateScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller

interface AccountUpdateController
    : Controller<UiAccountUpdateScreenState, AccountUpdateController> {

    suspend fun updateAccount(newAccountData: UiNewAccount)

}