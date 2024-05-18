package com.dirtfy.ppp.selling.tabling.model

import com.dirtfy.ppp.selling.menuManaging.model.MenuData

data class TableData(
    val tableNumber: Int,
    val menuNameList: List<String>,
    val menuPriceList: List<Int>
)
