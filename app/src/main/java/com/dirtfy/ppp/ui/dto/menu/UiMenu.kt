package com.dirtfy.ppp.ui.dto.menu

import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiMenu(
    val name: String,
    val price: String
) {

    companion object {
        fun DataMenu.convertToUiMenu(): UiMenu {
            return UiMenu(
                name = name,
                price = Utils.currencyFormatting(price),
            )
        }
    }

    fun convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name,
            price = Utils.currencyReformatting(price),
        )
    }
}