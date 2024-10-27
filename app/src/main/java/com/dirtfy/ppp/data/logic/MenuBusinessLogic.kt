package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.api.MenuApi
import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MenuBusinessLogic @Inject constructor(
    private val menuApi: MenuApi
): BusinessLogic {

    fun createMenu(menu: DataMenu) = flow {
        menuApi.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            emit(it.create(menu))
        }
    }

    fun readMenu() = flow {
        val menuList = menuApi.readAll()
        emit(menuList)
    }

    fun deleteMenu(menu: DataMenu) = flow {
        val deletedMenu = menuApi.delete(menu)
        emit(deletedMenu)
    }

    fun menuStream() = menuApi.menuStream()
}