package com.dirtfy.ppp.data.dto

import java.util.Calendar

data class DataRecord(
    val income: Int,
    val type: String,
    val issuedBy: String = "custom",
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
