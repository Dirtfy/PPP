package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiUpdateAccount

data class UiAccountScreenState(
    val accountList: List<UiAccount> = emptyList(),
    val searchClue: String = "",
    val nowAccount: UiAccount = UiAccount(),
    val newAccount: UiNewAccount = UiNewAccount(),
    val newAccountRecord: UiNewAccountRecord = UiNewAccountRecord(),
    val accountRecordList: List<UiAccountRecord> = emptyList(),
    val updateAccount: UiUpdateAccount = UiUpdateAccount(),
    val mode: UiAccountMode = UiAccountMode.Main,

    val accountListState: UiScreenState = UiScreenState(),
    val newAccountState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val numberGeneratingState: UiScreenState = UiScreenState(),
    val accountRecordListState: UiScreenState = UiScreenState(),
    val newAccountRecordState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val updateAccountState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
