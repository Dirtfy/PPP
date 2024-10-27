package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.data.source.repository.MenuRepository
import javax.inject.Inject

class MenuService @Inject constructor(
    private val menuRepository: MenuRepository
): Service {

    fun createMenu(menu: DataMenu) = operate {
        menuRepository.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            it.create(menu)
        }
    }

    fun readMenu() = operate {
        val menuList = menuRepository.readAll()
        menuList
    }

    fun deleteMenu(menu: DataMenu) = operate {
        val deletedMenu = menuRepository.delete(menu)
        deletedMenu
    }

    fun menuStream() = menuRepository.menuStream()
}