package com.dirtfy.ppp.ui.dto.account.screen

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord

data class UiAccountScreen(
    val accountList: FlowState<List<UiAccount>> = FlowState.loading(),
    val accountRecordList: FlowState<List<UiAccountRecord>> = FlowState.loading(),
    val newAccount: UiNewAccount = UiNewAccount(),
    val searchClue: String = "",

    val nowAccount: UiAccount = UiAccount(),
    val newAccountRecord: UiNewAccountRecord = UiNewAccountRecord(),

    val mode: UiAccountMode = UiAccountMode.Main
)
