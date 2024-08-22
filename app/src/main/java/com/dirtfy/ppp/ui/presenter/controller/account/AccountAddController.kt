package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountAddController: Controller {
    val newAccount: StateFlow<UiNewAccount>

    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

    fun request(job: suspend AccountAddController.() -> Unit)
}