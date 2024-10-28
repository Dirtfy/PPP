package com.dirtfy.ppp.data.api.impl.feature.table.firebase

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder

data class FireStoreTableOrder(
    val name: String?,
    val price: Int?,
    val count: Int?
) {
    constructor(): this(
        null,
        null,
        null
    )

    companion object {
        fun DataTableOrder.convertToFireStoreTableOrder(): FireStoreTableOrder {
            return FireStoreTableOrder(
                name = name,
                price = price,
                count = count
            )
        }
    }

    fun convertToDataTableOrder(): DataTableOrder {
        return DataTableOrder(
            name = name?: throw TableException.NameLoss(),
            price = price?: throw TableException.PriceLoss(),
            count = count?: throw TableException.CountLoss()
        )
    }

}
