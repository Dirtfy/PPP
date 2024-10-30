package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountCreateScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord

interface AccountCreateController
    : Controller<UiAccountCreateScreenState, AccountCreateController> {

    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    fun updateNewPhoneNumber(newPhoneNumber: String): Pair<String,Int>
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

}