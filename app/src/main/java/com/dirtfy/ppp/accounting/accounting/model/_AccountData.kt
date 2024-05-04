package com.dirtfy.ppp.accounting.accounting.model

import com.google.firebase.Timestamp

data class _AccountData(
    var accountName: String?,
    var phoneNumber: String?,
    var registerTimestamp: Timestamp?,
    var balance: Int?
) {
    // for firebase toObject
    constructor() : this(null, null, null, null)
}
