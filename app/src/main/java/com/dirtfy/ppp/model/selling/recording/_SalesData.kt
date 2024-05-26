package com.dirtfy.ppp.model.selling.recording

data class _SalesData(
    val menuNameList: ArrayList<String>?,
    val menuPriceList: ArrayList<Int>?,
    val pointAccountNumber: String?
) {
    constructor(): this(null, null, null)
}
