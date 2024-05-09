package com.dirtfy.ppp.accounting.accounting.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val accountID: String? = null,
    val accountName: String = "loading..",
    val phoneNumber: String = "...",
    val registerTimestamp: Long = 0L,
    val balance: Int = 0
)