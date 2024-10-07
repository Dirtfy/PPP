package com.dirtfy.ppp.ui.dto

import com.dirtfy.ppp.common.FlowState

data class UiAccountScreen(
    val accountList: FlowState<List<UiAccount>> = FlowState.loading(),
    val accountRecordList: FlowState<List<UiAccountRecord>> = FlowState.loading(),
    val newAccount: UiNewAccount = UiNewAccount(),
    val searchClue: String = "",

    val nowAccount: UiAccount = UiAccount(),
    val newAccountRecord: UiNewAccountRecord = UiNewAccountRecord(),

    val mode: UiAccountMode = UiAccountMode.Main
)
