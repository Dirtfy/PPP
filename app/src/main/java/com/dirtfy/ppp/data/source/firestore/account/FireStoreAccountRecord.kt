package com.dirtfy.ppp.data.source.firestore.account

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.DataAccountRecord

data class FireStoreAccountRecord(
    val accountNumber: Int?,
    val issuedName: String?,
    val difference: Int?,
    val result: Int?,
    val timestamp: Long?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null
    )

    companion object {
        fun DataAccountRecord.convertToFireStoreAccountRecord(
            accountNumber: Int
        ): FireStoreAccountRecord {
            return FireStoreAccountRecord(
                accountNumber = accountNumber,
                issuedName = issuedName,
                difference = difference,
                result = result,
                timestamp = timestamp
            )
        }
    }

    fun convertToDataAccountRecord(): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName?: throw AccountException.RecordIssuedNameLoss(),
            difference = difference?: throw AccountException.RecordAmountLoss(),
            result = result?: throw AccountException.RecordResultLoss(),
            timestamp = timestamp?: throw AccountException.RecordTimestampLoss()
        )
    }

}
