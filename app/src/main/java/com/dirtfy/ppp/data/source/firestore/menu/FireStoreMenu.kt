package com.dirtfy.ppp.data.source.firestore.menu

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.DataMenu

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
