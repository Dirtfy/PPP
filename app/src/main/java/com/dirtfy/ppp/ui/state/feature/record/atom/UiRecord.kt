package com.dirtfy.ppp.ui.state.feature.record.atom

import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordType
import com.dirtfy.ppp.ui.controller.common.Utils

data class UiRecord(
    val timestamp: String = "",
    val income: String = "",
    val type: String = ""
) {

    companion object {
        fun DataRecord.convertToRawUiRecord(): UiRecord {
            return UiRecord(
                timestamp = Utils.formatTimestampFromMillis(timestamp),
                income = Utils.formatCurrency(income),
                type = "$type - $issuedBy"
            )
        }

        fun DataRecord.convertToUiRecord(): UiRecord {
            return UiRecord(
                timestamp = Utils.formatTimestampFromMillis(timestamp),
                income = Utils.formatCurrency(income),
                type = if (type == DataRecordType.Cash.name
                    || type == DataRecordType.Card.name) {
                    type
                } else {
                    DataRecordType.Point.name
                }
            )
        }
    }

    fun convertToDataRecord(): DataRecord {
        return DataRecord(
            income = Utils.parseCurrency(income),
            type = type.split("-")[0],
            issuedBy = type.split("-")[1],
            timestamp = Utils.parseTimestampFromMillis(timestamp)
        )
    }

    fun convertToDataRecordFromRaw(): DataRecord {
        return DataRecord(
            income = Utils.parseCurrency(income),
            type = type.split("-")[0],
            issuedBy = type.split("-")[1],
            timestamp = Utils.parseTimestampFromMillis(timestamp)
        )
    }
}
