package com.dirtfy.ppp.model.selling.recording

data class SalesData(
    val salesID: String?,
    val menuCountMap: Map<String, Int>,
    val menuPriceMap: Map<String, Int>,
    val pointAccountNumber: String?
)
