package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.api.impl.common.firebase.Utils.convertToMilliseconds
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.google.firebase.Timestamp
import java.util.Date

data class FireStoreRecord(
    val timestamp: Timestamp?,
    val income: Int?,
    val type: String?,
    val issuedBy: String?
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
                income = income,
                type = type,
                issuedBy = issuedBy
            )
        }
    }

    fun convertToDataRecord(): DataRecord {
        return DataRecord(
            income = income ?: throw RecordException.IncomeLoss(),
            type = type?: throw RecordException.TypeLoss(),
            issuedBy = issuedBy ?: throw RecordException.IssuedNameLoss(),
            timestamp = timestamp?.convertToMilliseconds()?: throw RecordException.TimestampLoss()
        )
    }

    fun convertToDataAccountRecord(
        result: Int
    ): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedBy ?: throw RecordException.IssuedNameLoss(),
            difference = income ?: throw RecordException.DifferenceLoss(),
            result = result,
            timestamp = timestamp?.convertToMilliseconds()?: throw RecordException.TimestampLoss()
        )
    }
}
