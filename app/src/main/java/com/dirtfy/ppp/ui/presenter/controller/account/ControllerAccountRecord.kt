package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.data.logic.account.ServiceAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.Utils

data class ControllerAccountRecord(
    val accountNumber: String,
    val issuedName: String,
    val difference: String,
    val result: String,
    val timestamp: String
) {
    companion object {
        fun ServiceAccountRecord.convertToControllerAccountRecord(): ControllerAccountRecord {
            return ControllerAccountRecord(
                accountNumber = accountNumber.toString(),
                issuedName = issuedName,
                difference = Utils.currencyFormatting(difference),
                result = Utils.currencyFormatting(result),
                timestamp = Utils.timestampFormatting_YMDHm(timestamp)
            )
        }
    }

    fun convertToServiceAccountRecord(): ServiceAccountRecord {
        return ServiceAccountRecord(
            accountNumber = accountNumber.toInt(),
            issuedName = issuedName,
            difference = Utils.currencyReformatting(difference),
            result = Utils.currencyReformatting(result),
            timestamp = Utils.timestampReformatting_YMDHm(timestamp)
        )
    }
}
