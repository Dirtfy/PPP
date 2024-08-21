package com.dirtfy.ppp.ui.presenter.controller.menu

import com.dirtfy.ppp.data.logic.menu.ServiceMenu
import com.dirtfy.ppp.ui.presenter.controller.Utils

data class ControllerMenu(
    val name: String,
    val price: String
) {

    companion object {
        fun ServiceMenu.convertToControllerMenu(): ControllerMenu {
            return ControllerMenu(
                name = name,
                price = Utils.currencyFormatting(price),
            )
        }
    }

    fun convertToServiceMenu(): ServiceMenu {
        return ServiceMenu(
            name = name,
            price = Utils.currencyReformatting(price),
        )
    }
}