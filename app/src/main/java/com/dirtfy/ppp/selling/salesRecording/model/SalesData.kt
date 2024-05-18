package com.dirtfy.ppp.selling.salesRecording.model

import com.dirtfy.ppp.selling.menuManaging.model.MenuData

data class SalesData(
    val menuList: ArrayList<MenuData>,
    val pointAccountNumber: String?
)
