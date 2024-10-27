package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode

data class UiAccountScreenState(
    val accountList: List<UiAccount> = emptyList(),
    val searchClue: String = "",
    val nowAccount: UiAccount = UiAccount(),
    val mode: UiAccountMode = UiAccountMode.Main,

    val accountListState: UiScreenState = UiScreenState()
)
