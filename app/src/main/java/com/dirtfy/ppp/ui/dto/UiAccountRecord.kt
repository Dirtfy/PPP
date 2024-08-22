package com.dirtfy.ppp.ui.dto

import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiAccountRecord(
    val issuedName: String,
    val difference: String,
    val result: String,
    val timestamp: String
) {
    companion object {
        fun DataAccountRecord.convertToUiAccountRecord(): UiAccountRecord {
            return UiAccountRecord(
                issuedName = issuedName,
                difference = Utils.currencyFormatting(difference),
                result = Utils.currencyFormatting(result),
                timestamp = Utils.timestampFormatting_YMDHm(timestamp)
            )
        }
    }

    fun convertToDataAccountRecord(): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName,
            difference = Utils.currencyReformatting(difference),
            result = Utils.currencyReformatting(result),
            timestamp = Utils.timestampReformatting_YMDHm(timestamp)
        )
    }
}
