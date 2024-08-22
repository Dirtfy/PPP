package com.dirtfy.ppp.data.source.firestore.record

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.google.firebase.Timestamp

data class FireStoreRecord(
    val timestamp: Timestamp?,
    val amount: Int?,
    val type: String?,
    val issuedName: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )

    fun convertToDataAccountRecord(
        result: Int
    ): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName?: throw RecordException.IssuedNameLoss(),
            difference = amount?: throw RecordException.DifferenceLoss(),
            result = result,
            timestamp = timestamp?.seconds?.times(1000L)?: throw RecordException.TimestampLoss()
        )
    }
}
