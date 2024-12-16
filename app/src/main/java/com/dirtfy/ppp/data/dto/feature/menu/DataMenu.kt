package com.dirtfy.ppp.data.dto.feature.menu

data class DataMenu(
    val name: String,
    val price: Int,

    /** bit masked integer of enum class MenuCategory's member "code"
     * @see MenuCategory */
    val category: Int,
)
