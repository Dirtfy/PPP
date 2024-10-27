package com.dirtfy.ppp.data.api.impl.feature.menu.firebase

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.feature.menu.DataMenu

data class FireStoreMenu(
    val name: String?,
    val price: Int?
) {
    constructor() : this(null, null)

    companion object {
        fun DataMenu.convertToFireStoreMenu(): FireStoreMenu {
            return FireStoreMenu(
                name = name,
                price = price,
            )
        }
    }

    fun convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name?: throw MenuException.NameLoss(),
            price = price?: throw MenuException.PriceLoss()
        )
    }
}
