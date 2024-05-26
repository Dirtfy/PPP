package com.dirtfy.ppp.viewmodel.selling.tabling

data class TableOrderData(
    val countMap: Map<String, Int>,
    val priceMap: Map<String, Int>
) {

    fun getTotalPrice(): Int {
        var total = 0

        countMap.keys.toList().forEach {
            total += countMap[it]!! * priceMap[it]!!
        }

        return total
    }
}

