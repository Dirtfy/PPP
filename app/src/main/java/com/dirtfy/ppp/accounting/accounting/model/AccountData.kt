package com.dirtfy.ppp.accounting.accounting.model

import com.google.firebase.Timestamp

data class AccountData(
    val accountID: String?,
    val accountName: String,
    val phoneNumber: String,
    val registerTimestamp: Timestamp,
    val balance: Int
)