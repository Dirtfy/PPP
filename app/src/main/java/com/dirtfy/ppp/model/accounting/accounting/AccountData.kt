package com.dirtfy.ppp.model.accounting.accounting

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val accountID: String? = null,
    val accountNumber: String = "...",
    val accountName: String = "loading..",
    val phoneNumber: String = "...",
    val registerTimestamp: Long = 0L,
    val balance: Int = 0
)