package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord

interface AccountController
    : Controller<UiAccountScreenState, AccountController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountList()

    suspend fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount(newAccountData: UiNewAccount)
    suspend fun setRandomValidAccountNumberToNewAccount()

    suspend fun updateAccountRecordList()
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)
    suspend fun addRecord(newAccountRecord: UiNewAccountRecord)

    suspend fun updateAccount(newAccountData: UiNewAccount)

    fun updateNowAccount(account: UiAccount)
    fun updateSearchClue(clue: String)
    fun setMode(mode: UiAccountMode)

}