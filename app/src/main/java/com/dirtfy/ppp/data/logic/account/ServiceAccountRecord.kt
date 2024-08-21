package com.dirtfy.ppp.data.logic.account

import java.util.Calendar

data class ServiceAccountRecord(
    val issuedName: String,
    val difference: Int,
    val result: Int,
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
