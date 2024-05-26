package com.dirtfy.ppp.model.selling.tabling

data class TableData(
    val tableNumber: Int,
    val menuNameList: List<String>,
    val menuPriceList: List<Int>
)
