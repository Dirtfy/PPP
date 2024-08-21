package com.dirtfy.ppp.data.logic.menu

import com.dirtfy.ppp.data.source.repository.menu.RepositoryMenu

data class ServiceMenu(
    val name: String,
    val price: Int
) {
    companion object {
        fun RepositoryMenu.convertToServiceMenu(): ServiceMenu {
            return ServiceMenu(
                name = name?: throw MenuException.NameLoss(),
                price = price?: throw MenuException.PriceLoss()
            )
        }
    }
}
