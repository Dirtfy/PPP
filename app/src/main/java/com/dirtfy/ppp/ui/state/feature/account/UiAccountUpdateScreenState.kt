package com.dirtfy.ppp.ui.state.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiUpdateAccount

data class UiAccountUpdateScreenState(
    val updateAccount: UiUpdateAccount = UiUpdateAccount(),

    val updateAccountState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
