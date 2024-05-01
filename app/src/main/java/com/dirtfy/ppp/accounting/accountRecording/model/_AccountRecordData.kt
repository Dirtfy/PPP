package com.dirtfy.ppp.accounting.accountRecording.model

import com.google.firebase.Timestamp

data class _AccountRecordData(
    var timestamp: Timestamp?,
    var accountID: String?,
    var userName: String?,
    var amount: Int?,
    var result: Int?
)
