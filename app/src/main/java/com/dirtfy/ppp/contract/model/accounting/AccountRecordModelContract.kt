package com.dirtfy.ppp.contract.model.accounting

import com.dirtfy.ppp.common.Repository
import com.google.firebase.Timestamp
import java.util.Date

object AccountRecordModelContract {

    object DTO {
        data class AccountRecord(
            val recordID: String? = null,
            val accountNumber: String = "...",
            val timestamp: Timestamp = Timestamp(Date()),
            val userName: String = "loading..",
            val amount: Int = 0,
            val result: Int = 0
        )
    }

    interface API: Repository<DTO.AccountRecord>
}