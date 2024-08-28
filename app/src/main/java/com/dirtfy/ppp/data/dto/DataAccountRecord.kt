package com.dirtfy.ppp.data.dto

import java.util.Calendar

data class DataAccountRecord(
    val issuedName: String,
    val difference: Int,
    val result: Int,
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
