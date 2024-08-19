package com.dirtfy.ppp.model.accounting.accounting

import com.google.firebase.Timestamp

data class _AccountData(
    var accountNumber: String?,
    var accountName: String?,
    var phoneNumber: String?,
    var registerTimestamp: Timestamp?,
    var balance: Int?
) {
    // for firebase toObject
    constructor()
            : this(null,
        null, null,
        null, null)
}
