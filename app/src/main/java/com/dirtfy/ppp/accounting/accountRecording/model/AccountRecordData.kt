package com.dirtfy.ppp.accounting.accountRecording.model

import com.google.firebase.Timestamp
import java.util.Date

data class AccountRecordData(
    val recordID: String? = null,
    val accountNumber: String = "...",
    val timestamp: Timestamp = Timestamp(Date()),
    val userName: String = "loading..",
    val amount: Int = 0,
    val result: Int = 0
)
