package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.account.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord

interface AccountDetailController
    : Controller<UiAccountDetailScreenState, AccountDetailController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountRecordList()
    suspend fun updateNowAccount(account: UiAccount)
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)

    suspend fun addRecord(newAccountRecord: UiNewAccountRecord)

}