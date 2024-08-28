package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.data.source.repository.MenuRepository

class MenuService(
    private val menuRepository: MenuRepository
): Service {

    fun createMenu(menu: DataMenu) = asFlow {
        menuRepository.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            it.create(menu)
        }
    }

    fun readMenu() = asFlow {
        menuRepository.readAll()
    }

    fun deleteMenu(menu: DataMenu) = asFlow {
        menuRepository.delete(menu)
    }
}