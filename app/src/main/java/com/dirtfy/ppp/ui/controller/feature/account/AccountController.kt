package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode

interface AccountController
    : Controller<UiAccountScreenState, AccountController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountList()
    fun updateNowAccount(account: UiAccount)
    fun updateSearchClue(clue: String)

    fun setMode(mode: UiAccountMode)

}