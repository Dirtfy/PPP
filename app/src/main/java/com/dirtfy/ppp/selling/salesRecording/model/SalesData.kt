package com.dirtfy.ppp.selling.salesRecording.model

data class SalesData(
    val salesID: String?,
    val menuCountMap: Map<String, Int>,
    val menuPriceMap: Map<String, Int>,
    val pointAccountNumber: String?
)
