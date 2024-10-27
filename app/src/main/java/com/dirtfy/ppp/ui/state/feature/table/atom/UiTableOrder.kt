package com.dirtfy.ppp.ui.state.feature.table.atom

import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiTableOrder(
    val name: String,
    val price: String,
    val count: String
) {
    companion object {
        fun DataTableOrder.convertToUiTableOrder(): UiTableOrder {
            return UiTableOrder(
                name = name,
                price = Utils.formatCurrency(price),
                count = count.toString()
            )
        }
    }

    fun convertToDataTableOrder(): DataTableOrder {
        return DataTableOrder(
            name = name,
            price = Utils.parseCurrency(price),
            count = count.toInt()
        )
    }
}
