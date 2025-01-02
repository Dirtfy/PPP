package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord

interface AccountController
    : Controller<UiAccountScreenState, AccountController> {

    // AccountListController
    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountList()
    fun retryUpdateAccountList()
    fun updateSearchClue(clue: String)

    // AccountCreateController
    fun updateNewAccount(newAccountData: UiNewAccount)
    suspend fun addAccount()
    suspend fun setRandomValidAccountNumberToNewAccount()

    // AccountDetailController
    fun updateNowAccount(account: UiAccount)
    @Deprecated(
        message = "accountRecordList will be automatically updated when nowAccount is updated",
        replaceWith = ReplaceWith("updateNowAccount(account)")
    )
    fun retryUpdateAccountRecordList()
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)
    suspend fun addRecord()

    // AccountUpdateController
    suspend fun updateAccount(newAccountData: UiNewAccount)

    // AccountController
    fun setMode(mode: UiAccountMode)

    // state setters
    fun setAddAccountState(state: UiScreenState)
    fun setNumberGeneratingState(state: UiScreenState)
    fun setAccountRecordListState(state: UiScreenState)
    fun setAddAccountRecordState(state: UiScreenState)
    fun setAccountListState(state: UiScreenState)
}