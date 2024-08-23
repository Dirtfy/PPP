package com.dirtfy.ppp.data.source.firestore.record

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.source.firestore.Utils.convertToMilliseconds
import com.google.firebase.Timestamp
import java.util.Date

data class FireStoreRecord(
    val timestamp: Timestamp?,
    val millisecond: Long?,
    val amount: Int?,
    val type: String?,
    val issuedName: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null
    )

    companion object {
        fun DataRecord.convertToFireStoreRecord(
            type: String? = null
        ): FireStoreRecord {
            return FireStoreRecord(
                timestamp = Timestamp(Date(timestamp)),
                millisecond = timestamp,
                amount = income,
                issuedName = null,
                type = type?: this.type.name
            )
        }
        fun DataRecord.convertToFireStoreRecord(
            issuedName: String?,
            type: String? = null
        ): FireStoreRecord {
            return FireStoreRecord(
                timestamp = Timestamp(Date(timestamp)),
                millisecond = timestamp,
                amount = income,
                issuedName = issuedName,
                type = type?: this.type.name
            )
        }
    }

    fun convertToDataRecord(): DataRecord {
        return DataRecord(
            timestamp = timestamp?.convertToMilliseconds()?: throw RecordException.TimestampLoss(),
            income = amount?: throw RecordException.IncomeLoss(),
            type = if (type in DataRecordType.entries.map { it.name }) {
                DataRecordType.entries.filter { it.name == type }[0]
            } else {
                DataRecordType.Point
            }
        )
    }

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
