package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount

data class UiAccountCreateScreenState(
    val newAccount: UiNewAccount = UiNewAccount(),
    val newAccountState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val numberGeneratingState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
