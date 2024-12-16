package com.dirtfy.ppp.data.dto.feature.menu

enum class MenuCategory(
    /** Korean name of the Category
     * must be same with string resources */
    val koName: String,

    /** code should be a power of 2 */
    val code: Int
) {
    ALCOHOL("주류", 1),
    LUNCH("런치", 2),
    DINNER("디너", 4)
}