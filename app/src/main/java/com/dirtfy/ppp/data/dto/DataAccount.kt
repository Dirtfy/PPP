package com.dirtfy.ppp.data.dto

import java.util.Calendar

data class DataAccount(
    val number: Int,
    val name: String,
    val phoneNumber: String,
    val balance: Int = 0,
    val registerTimestamp: Long = Calendar.getInstance().timeInMillis
)
