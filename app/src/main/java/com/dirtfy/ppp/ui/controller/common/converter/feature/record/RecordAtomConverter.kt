package com.dirtfy.ppp.ui.controller.common.converter.feature.record

import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.ppp.data.dto.feature.record.DataRecordType
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail

object RecordAtomConverter {

    fun DataRecord.convertToRawUiRecord(): UiRecord {
        return UiRecord(
            timestamp = StringFormatConverter.formatTimestampFromMillis(timestamp),
            income = StringFormatConverter.formatCurrency(income),
            type = "$type - $issuedBy"
        )
    }

    fun DataRecord.convertToUiRecord(): UiRecord {
        return UiRecord(
            timestamp = StringFormatConverter.formatTimestampFromMillis(timestamp),
            income = StringFormatConverter.formatCurrency(income),
            type = if (type == DataRecordType.Cash.name
                || type == DataRecordType.Card.name) {
                type
            } else {
                DataRecordType.Point.name
            }
        )
    }

    fun DataRecordDetail.convertToUiRecordDetail(): UiRecordDetail {
        return UiRecordDetail(
            name = name,
            price = StringFormatConverter.formatCurrency(amount),
            count = count.toString()
        )
    }



    fun UiRecord.convertToDataRecord(): DataRecord {
        return DataRecord(
            income = StringFormatConverter.parseCurrency(income),
            type = type.split("-")[0],
            issuedBy = type.split("-")[1],
            timestamp = StringFormatConverter.parseTimestampFromMillis(timestamp)
        )
    }

    fun UiRecord.convertToDataRecordFromRaw(): DataRecord {
        return DataRecord(
            income = StringFormatConverter.parseCurrency(income),
            type = type.split("-")[0],
            issuedBy = type.split("-")[1],
            timestamp = StringFormatConverter.parseTimestampFromMillis(timestamp)
        )
    }

}