package com.dirtfy.ppp.ui.dto.record

import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

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
                type = if (type == com.dirtfy.ppp.data.dto.feature.record.DataRecordType.Cash.name
                    || type == com.dirtfy.ppp.data.dto.feature.record.DataRecordType.Card.name) {
                    type
                } else {
                    com.dirtfy.ppp.data.dto.feature.record.DataRecordType.Point.name
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
