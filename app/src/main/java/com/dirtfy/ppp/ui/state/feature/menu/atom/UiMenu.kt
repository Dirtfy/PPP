package com.dirtfy.ppp.ui.state.feature.menu.atom

import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import com.dirtfy.ppp.ui.controller.common.Utils

data class UiMenu(
    val name: String = "",
    val price: String = ""
) {

    companion object {
        fun DataMenu.convertToUiMenu(): UiMenu {
            return UiMenu(
                name = name,
                price = Utils.formatCurrency(price),
            )
        }
    }

    fun convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name,
            price = Utils.parseCurrency(price),
        )
    }
}