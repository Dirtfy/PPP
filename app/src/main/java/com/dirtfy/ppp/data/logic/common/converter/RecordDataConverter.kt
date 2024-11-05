package com.dirtfy.ppp.data.logic.common.converter

import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecord

object RecordDataConverter {

    fun DataRecord.convertToDataAccountRecord(
        result: Int
    ): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedBy,
            difference = income,
            timestamp = timestamp,
            result = result
        )
    }
}