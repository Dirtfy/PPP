package com.dirtfy.ppp.test.ui.dto.account

import com.dirtfy.ppp.ui.dto.UiAccountMode

data class UiAccountState(
    val accountList: List<UiAccount> = emptyList(),
    val searchClue: String = "",
    val mode: UiAccountMode = UiAccountMode.Main,
    val failMassage: String = "",
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
