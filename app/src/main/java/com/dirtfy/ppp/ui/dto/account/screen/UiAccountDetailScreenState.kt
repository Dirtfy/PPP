package com.dirtfy.ppp.ui.dto.account.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord

data class UiAccountDetailScreenState(
    val nowAccount: UiAccount = UiAccount(),
    val newAccountRecord: UiNewAccountRecord = UiNewAccountRecord(),
    val accountRecordList: List<UiAccountRecord> = emptyList(),

    val accountRecordListState: UiScreenState = UiScreenState()
)
