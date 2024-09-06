package com.dirtfy.ppp.test.ui.dto.account

import com.dirtfy.ppp.ui.dto.UiRecord

data class UiAccountDetailState(
    val detail: UiAccount = UiAccount(),
    val balance: String = "",
    val userName: String = "",
    val difference: String = "",
    val recordList: List<UiRecord> = emptyList(),
    val failMassage: String = "",
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
