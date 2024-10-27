package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.api.impl.common.firebase.Utils.convertToMilliseconds
import com.google.firebase.Timestamp
import java.util.Date

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
        null,
    )

    companion object {
        fun DataRecord.convertToFireStoreRecord(): FireStoreRecord {
            return FireStoreRecord(
                timestamp = Timestamp(Date(timestamp)),
                amount = income,
                type = type,
                issuedName = issuedBy
            )
        }
    }

    fun convertToDataRecord(): DataRecord {
        return DataRecord(
            income = amount ?: throw RecordException.IncomeLoss(),
            type = type?: throw RecordException.TypeLoss(),
            issuedBy = issuedName?: throw RecordException.IssuedNameLoss(),
            timestamp = timestamp?.convertToMilliseconds()?: throw RecordException.TimestampLoss()
        )
    }

    fun convertToDataAccountRecord(
        result: Int
    ): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName ?: throw RecordException.IssuedNameLoss(),
            difference = amount ?: throw RecordException.DifferenceLoss(),
            result = result,
            timestamp = timestamp?.convertToMilliseconds()?: throw RecordException.TimestampLoss()
        )
    }
}
