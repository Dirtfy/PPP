package com.dirtfy.ppp.selling.salesRecording.model

data class _SalesData(
    val menuNameList: ArrayList<String>?,
    val menuPriceList: ArrayList<Int>?,
    val pointAccountNumber: String?
) {
    constructor(): this(null, null, null)
}
