package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import kotlinx.coroutines.flow.Flow

interface AccountDetailController {
    val screenData: Flow<UiAccountDetailScreenState>

    fun updateNowAccount(account: UiAccount)
    @Deprecated(
        message = "accountRecordList will be automatically updated when nowAccount is updated",
        replaceWith = ReplaceWith("updateNowAccount(account)")
    )
    fun updateAccountRecordList(account: UiAccount)
    fun retryUpdateAccountRecordList()
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)
    suspend fun addRecord()

    fun setAccountRecordListState(state: UiScreenState)
    fun setAddAccountRecordState(state: UiScreenState)
}