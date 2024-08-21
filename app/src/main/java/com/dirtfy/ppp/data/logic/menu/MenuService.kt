package com.dirtfy.ppp.data.logic.menu

import com.dirtfy.ppp.data.logic.Service
import com.dirtfy.ppp.data.logic.menu.ServiceMenu.Companion.convertToServiceMenu
import com.dirtfy.ppp.data.source.repository.menu.MenuRepository
import com.dirtfy.ppp.data.source.repository.menu.RepositoryMenu

class MenuService(
    val menuRepository: MenuRepository
): Service {

    fun createMenu(menu: ServiceMenu) = asFlow {
        menuRepository.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            it.create(RepositoryMenu(
                name = menu.name,
                price = menu.price
            )).convertToServiceMenu()
        }
    }

    fun readMenu() = asFlow {
        menuRepository.readAll().map {
            it.convertToServiceMenu()
        }
    }

    fun deleteMenu(menu: ServiceMenu) = asFlow {
        menuRepository.delete(
            RepositoryMenu(
                name = menu.name,
                price = menu.price
            )
        ).convertToServiceMenu()
    }
}