package com.dirtfy.ppp.data.dto

data class DataRecord(
    val timestamp: Long,
    val income: Int,
    val type: String,
    val issuedBy: String = "custom"
)
