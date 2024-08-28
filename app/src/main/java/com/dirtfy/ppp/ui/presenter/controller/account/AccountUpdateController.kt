package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountUpdateController: Controller {
    val nowAccount: StateFlow<UiAccount>

    suspend fun updateAccount(newAccountData: UiNewAccount)
}