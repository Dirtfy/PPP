package com.dirtfy.ppp.ui.dto.account.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiUpdateAccount

data class UiAccountUpdateScreenState(
    val updateAccount: UiUpdateAccount = UiUpdateAccount(),

    val updateAccountState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
