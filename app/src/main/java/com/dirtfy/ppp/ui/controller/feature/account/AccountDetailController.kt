package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

interface AccountDetailController {
    val screenData: Flow<UiAccountDetailScreenState>

    suspend fun updateAccountRecordList(account: UiAccount)
    fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)
    suspend fun addRecord(newAccountRecord: UiNewAccountRecord)

    fun setAccountRecordListState(state: UiScreenState)
    fun setNewAccountRecordState(state: UiScreenState)
}