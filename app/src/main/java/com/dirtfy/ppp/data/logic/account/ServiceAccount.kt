package com.dirtfy.ppp.data.logic.account

import java.util.Calendar

data class ServiceAccount(
    val number: Int,
    val name: String,
    val phoneNumber: String,
    val balance: Int = 0,
    val registerTimestamp: Long = Calendar.getInstance().timeInMillis,
    val recordList: List<ServiceAccountRecord> = emptyList()
)
