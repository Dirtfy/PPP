package com.dirtfy.ppp.contract.model.accounting

import com.dirtfy.ppp.common.Repository
import java.util.Calendar

object AccountModelContract {

    object DTO {
        data class Account(
            val accountID: String? = null,
            val accountNumber: String = "...",
            val accountName: String = "loading..",
            val phoneNumber: String = "...",
            val registerTimestamp: Long = Calendar.getInstance().timeInMillis,
            val balance: Int = 0
        )
    }

    interface API: Repository<DTO.Account>
}