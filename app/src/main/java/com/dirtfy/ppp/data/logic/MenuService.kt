package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.data.source.repository.MenuRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MenuService @Inject constructor(
    private val menuRepository: MenuRepository
): Service {

    fun createMenu(menu: DataMenu) = flow {
        menuRepository.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            emit(it.create(menu))
        }
    }

    fun readMenu() = flow {
        val menuList = menuRepository.readAll()
        emit(menuList)
    }

    fun deleteMenu(menu: DataMenu) = flow {
        val deletedMenu = menuRepository.delete(menu)
        emit(deletedMenu)
    }

    fun menuStream() = menuRepository.menuStream()
}