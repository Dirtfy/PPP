package com.dirtfy.ppp.data.dto.feature.record

import java.util.Calendar

data class DataRecord(
    val id: Int,
    val income: Int,
    val type: String,
    val issuedBy: String = "custom",
    val timestamp: Long = Calendar.getInstance().timeInMillis
)
