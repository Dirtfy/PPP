package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount

data class UiAccountListScreenState(
    val accountList: List<UiAccount> = emptyList(),
    val searchClue: String = "",

    val accountListState: UiScreenState = UiScreenState()
)
