package com.dirtfy.ppp.selling.tabling.model

data class TableData(
    val tableNumber: Int,
    val menuNameList: List<String>,
    val menuPriceList: List<Int>
)
