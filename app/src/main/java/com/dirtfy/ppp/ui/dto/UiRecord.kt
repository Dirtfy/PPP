package com.dirtfy.ppp.ui.dto

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiRecord(
    val timestamp: String,
    val income: String,
    val type: String,
    val millisecond: Long
) {

    companion object {
        fun DataRecord.convertToUiRecord(): UiRecord {
            return UiRecord(
                timestamp = Utils.timestampFormatting_YMDHmms(timestamp),
                income = Utils.currencyFormatting(income),
                type = if (type in listOf(DataRecordType.Card, DataRecordType.Cash)) {
                    type.name
                } else {
                    DataRecordType.Point.name
                },
                millisecond = timestamp
            )
        }
    }

    fun convertToDataRecord(): DataRecord {
        return DataRecord(
            timestamp = millisecond,
            income = Utils.currencyReformatting(income),
            type = if (type in DataRecordType.entries.map{ it.name}) {
                DataRecordType.entries.filter { it.name == type }[0]
            } else {
                DataRecordType.Card
            }
        )
    }
}
