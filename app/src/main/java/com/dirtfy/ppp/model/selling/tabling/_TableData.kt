package com.dirtfy.ppp.model.selling.tabling

data class _TableData(
    val menuNameList: ArrayList<String>?,
    val menuPriceList: ArrayList<Int>?
) {
    constructor(): this(ArrayList(), ArrayList())
}
