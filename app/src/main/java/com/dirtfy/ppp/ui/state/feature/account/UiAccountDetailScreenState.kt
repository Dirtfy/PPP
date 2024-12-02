package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord

data class UiAccountDetailScreenState(
    val nowAccount: UiAccount = UiAccount(),
    val newAccountRecord: UiNewAccountRecord = UiNewAccountRecord(),
    val accountRecordList: List<UiAccountRecord> = emptyList(),

    val accountRecordListState: UiScreenState = UiScreenState(),
    val addAccountRecordState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
