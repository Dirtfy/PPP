package com.dirtfy.ppp.accounting.accounting.model

import com.google.firebase.Timestamp
import java.util.Date

data class AccountData(
    val accountID: String? = null,
    val accountName: String = "loading..",
    val phoneNumber: String = "...",
    val registerTimestamp: Timestamp = Timestamp(Date()),
    val balance: Int = 0
)