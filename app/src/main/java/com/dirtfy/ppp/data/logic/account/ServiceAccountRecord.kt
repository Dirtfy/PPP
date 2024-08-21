package com.dirtfy.ppp.data.logic.account

import com.dirtfy.ppp.data.source.repository.account.record.RepositoryAccountRecord
import java.util.Calendar

data class ServiceAccountRecord(
    val accountNumber: Int,
    val issuedName: String,
    val difference: Int,
    val result: Int,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) {

    companion object {
        fun RepositoryAccountRecord.convertToServiceAccountRecord(): ServiceAccountRecord {
            return ServiceAccountRecord(
                accountNumber = accountNumber?: throw AccountException.RecordAccountNumberLoss(),
                issuedName = issuedName?: throw AccountException.RecordIssuedNameLoss(),
                difference = difference?: throw AccountException.RecordAmountLoss(),
                result = result?: throw AccountException.RecordResultLoss(),
                timestamp = timestamp?: throw AccountException.RecordTimestampLoss()
            )
        }
    }

    fun convertToRepositoryAccountRecord(): RepositoryAccountRecord {
        return RepositoryAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference,
            result = result,
            timestamp = timestamp
        )
    }
}
