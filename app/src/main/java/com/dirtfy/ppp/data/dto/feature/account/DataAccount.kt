package com.dirtfy.ppp.data.dto.feature.account

import com.dirtfy.ppp.common.exception.AccountException
import java.util.Calendar

data class DataAccount(
    val number: Int,
    val name: String,
    val phoneNumber: String,
    @Deprecated("balance is aggregated from records")
    val balance: Int = 0,
    val registerTimestamp: Long = Calendar.getInstance().timeInMillis
)
