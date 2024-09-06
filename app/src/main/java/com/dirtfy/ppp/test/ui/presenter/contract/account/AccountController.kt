package com.dirtfy.ppp.test.ui.presenter.contract.account

import com.dirtfy.ppp.test.ui.dto.account.UiAccount
import com.dirtfy.ppp.test.ui.dto.account.UiAccountState
import com.dirtfy.ppp.test.ui.presenter.contract.Controller
import com.dirtfy.ppp.ui.dto.UiAccountMode

interface AccountController: Controller<UiAccountState> {

    val selectedAccount: UiAccount

    fun updateSearchClue(clue: String)

    fun setMode(targetMode: UiAccountMode)

    fun onAccountClick(account: UiAccount)

}