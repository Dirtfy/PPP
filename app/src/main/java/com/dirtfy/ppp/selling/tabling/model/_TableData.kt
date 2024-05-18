package com.dirtfy.ppp.selling.tabling.model

data class _TableData(
    val menuNameList: ArrayList<String>?,
    val menuPriceList: ArrayList<Int>?
) {
    constructor(): this(ArrayList(), ArrayList())
}
