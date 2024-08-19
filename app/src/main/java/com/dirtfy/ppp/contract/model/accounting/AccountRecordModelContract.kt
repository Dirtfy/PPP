package com.dirtfy.ppp.contract.model.accounting

import com.dirtfy.ppp.common.Repository
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

object AccountRecordModelContract {

    object DTO {
        data class AccountRecord(
            val recordID: String? = null,
            val accountNumber: String = "...",
            val timestamp: Long = Calendar.getInstance().timeInMillis,
            val userName: String = "loading..",
            val amount: Int = 0,
            val result: Int = 0
        )
    }

    interface API: Repository<DTO.AccountRecord>
}