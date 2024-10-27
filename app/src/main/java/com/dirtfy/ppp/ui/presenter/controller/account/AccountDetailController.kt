package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountDetailController
    : Controller<UiAccountDetailScreenState, AccountDetailController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountRecordList()
    suspend fun updateNowAccount(account: UiAccount)
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)

    suspend fun addRecord(newAccountRecord: UiNewAccountRecord)

}