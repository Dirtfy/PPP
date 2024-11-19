package com.dirtfy.ppp.ui.controller.common.converter.feature.record

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.ppp.data.dto.feature.record.DataRecordType
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail

object RecordAtomConverter {

    fun DataRecord.convertToRawUiRecord(): UiRecord {
        return UiRecord(
            id = id.toString(),
            timestamp = StringFormatConverter.formatTimestampFromMillis(timestamp),
            income = StringFormatConverter.formatCurrency(income),
            type = "$type - $issuedBy"
        )
    }

    fun DataRecord.convertToUiRecord(): UiRecord {
        return UiRecord(
            id = id.toString(),
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
            id = try { id.toInt() } catch (e: Exception) { throw RecordException.IdLoss() },
            income = StringFormatConverter.parseCurrency(income),
            type = type.split("-")[0].trim(),
            issuedBy = type.split("-")[1].trim(),
            timestamp = StringFormatConverter.parseTimestampFromMillis(timestamp)
        )
    }

    fun UiRecord.convertToDataRecordFromRaw(): DataRecord {
        return DataRecord(
            id = try { id.toInt() } catch (e: Exception) { throw RecordException.IdLoss() },
            income = StringFormatConverter.parseCurrency(income),
            type = type.split("-")[0].trim(),
            issuedBy = type.split("-")[1].trim(),
            timestamp = StringFormatConverter.parseTimestampFromMillis(timestamp)
        )
    }

    fun DataRecord.convertToNowRecord(): UiRecord {
        return UiRecord(
            id = id.toString(),
            timestamp = StringFormatConverter.formatTimestampFromMinute(timestamp),
            income = StringFormatConverter.formatCurrency(income),
            type = if (type.split("-")[0].trim().matches(Regex("""^\d+$"""))) type
                   else type.split("-")[0].trim()
        )
    }

    fun UiRecord.convertToDataRecordFromNowRecord(): DataRecord {
        return DataRecord(
            id = id.toInt(),
            timestamp = StringFormatConverter.parseTimestampFromMinute(timestamp),
            income = StringFormatConverter.parseCurrency(income),
            type = if (type.split("-").size > 1) type.split("-")[0].trim()
                   else type,
            issuedBy = if (type.split("-").size > 1) type.split("-")[1].trim()
                       else "custom"
        )
    }

}