package com.dirtfy.ppp.ui.dto.account.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiNewAccount

data class UiAccountCreateScreenState(
    val newAccount: UiNewAccount = UiNewAccount(),

    val newAccountState: UiScreenState = UiScreenState(UiState.COMPLETE),
    val numberGeneratingState: UiScreenState = UiScreenState()
)
