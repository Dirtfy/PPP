package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountController
    : Controller<UiAccountScreenState, AccountController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountList()
    fun updateNowAccount(account: UiAccount)
    fun updateSearchClue(clue: String)

    fun setMode(mode: UiAccountMode)

}