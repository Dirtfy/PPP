package com.dirtfy.ppp.accounting.accountRecording.model

import com.google.firebase.Timestamp

data class AccountRecordData(
    val recordID: String,
    val accountID: String,
    val timestamp: Timestamp,
    val userName: String,
    val amount: Int,
    val result: Int
)
