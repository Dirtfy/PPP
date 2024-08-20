package com.dirtfy.ppp.model.selling.recording

import com.google.firebase.Timestamp

data class _SalesData(
    val menuNameList: ArrayList<String>?,
    val menuPriceList: ArrayList<Int>?,
    val pointAccountNumber: String?,
    val timestamp: Timestamp?
) {
    constructor(): this(null, null, null, null,)
}
