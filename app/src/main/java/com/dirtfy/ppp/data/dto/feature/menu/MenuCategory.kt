package com.dirtfy.ppp.data.dto.feature.menu

enum class MenuCategory(
    /** Korean name of the Category
     * must be same with string resources */
    val koName: String,
) {
    ALCOHOL("주류"),
    LUNCH("런치"),
    DINNER("디너");

    val code: Int
        get() = getCategoryCode()

    private fun getCategoryCode(): Int {
        var res = 1
        for (i in 0 until this.ordinal) res *= 2
        return res
    }
}