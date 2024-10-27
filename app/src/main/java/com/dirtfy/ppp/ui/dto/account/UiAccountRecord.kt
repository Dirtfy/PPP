package com.dirtfy.ppp.ui.dto.account

import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
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
                difference = Utils.formatCurrency(difference),
                result = Utils.formatCurrency(result),
                timestamp = Utils.formatTimestampFromMinute(timestamp)
            )
        }
    }

    fun convertToDataAccountRecord(): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName,
            difference = Utils.parseCurrency(difference),
            result = Utils.parseCurrency(result),
            timestamp = Utils.parseTimestampFromMinute(timestamp)
        )
    }
}
