package com.dirtfy.ppp.ui.dto.record

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiRecord(
    val timestamp: String,
    val income: String,
    val type: String
) {

    companion object {
        fun DataRecord.convertToRawUiRecord(): UiRecord {
            return UiRecord(
                timestamp = Utils.formatTimestampFromSecond(timestamp),
                income = Utils.formatCurrency(income),
                type = "$type - $issuedBy"
            )
        }

        fun DataRecord.convertToUiRecord(): UiRecord {
            return UiRecord(
                timestamp = Utils.formatTimestampFromMinute(timestamp),
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
            timestamp = Utils.parseTimestampFromMinute(timestamp)
        )
    }

    fun convertToDataRecordFromRaw(): DataRecord {
        return DataRecord(
            income = Utils.parseCurrency(income),
            type = type.split("-")[0],
            issuedBy = type.split("-")[1],
            timestamp = Utils.parseTimestampFromSecond(timestamp)
        )
    }
}
