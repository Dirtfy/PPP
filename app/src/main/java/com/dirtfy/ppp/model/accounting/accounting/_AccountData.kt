package com.dirtfy.ppp.model.accounting.accounting

data class _AccountData(
    var accountNumber: String?,
    var accountName: String?,
    var phoneNumber: String?,
    var registerTimestamp: Long?,
    var balance: Int?
) {
    // for firebase toObject
    constructor()
            : this(null,
        null, null,
        null, null)
}
