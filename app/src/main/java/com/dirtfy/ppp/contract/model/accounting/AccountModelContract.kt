package com.dirtfy.ppp.contract.model.accounting

import com.dirtfy.ppp.common.Repository

object AccountModelContract {

    object DTO {
        data class Account(
            val accountID: String? = null,
            val accountNumber: String = "...",
            val accountName: String = "loading..",
            val phoneNumber: String = "...",
            val registerTimestamp: Long = 0L,
            val balance: Int = 0
        )
    }

    interface API: Repository<DTO.Account>
}