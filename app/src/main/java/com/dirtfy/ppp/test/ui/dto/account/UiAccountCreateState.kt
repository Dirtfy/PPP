package com.dirtfy.ppp.test.ui.dto.account

data class UiAccountCreateState(
    val accountNumber: String = "",
    val accountName: String = "",
    val phoneNumber: String = "",
    val failMassage: String = "",
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
