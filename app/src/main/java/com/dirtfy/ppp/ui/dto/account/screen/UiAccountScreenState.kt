package com.dirtfy.ppp.ui.dto.account.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode

data class UiAccountScreenState(
    val accountList: List<UiAccount> = emptyList(),
    val searchClue: String = "",
    val nowAccount: UiAccount = UiAccount(),
    val mode: UiAccountMode = UiAccountMode.Main,

    val accountListState: UiScreenState = UiScreenState()
)
