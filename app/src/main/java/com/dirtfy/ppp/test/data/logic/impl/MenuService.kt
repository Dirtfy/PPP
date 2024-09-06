package com.dirtfy.ppp.test.data.logic.impl

import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.test.data.logic.MenuLogic
import com.dirtfy.ppp.test.data.source.MenuSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MenuService @Inject constructor(
    private val menuSource: MenuSource
): MenuLogic {

    override fun createMenu(menu: DataMenu) = flow<DataMenu> {
        menuSource.let {
            if (it.isSameNameExist(menu.name))
                throw MenuException.NonUniqueName()

            it.create(menu)
        }
    }

    override fun readMenu() = flow<List<DataMenu>> {
        menuSource.readAll()
    }

    override fun deleteMenu(menu: DataMenu) = flow<DataMenu> {
        menuSource.delete(menu)
    }

    override fun menuStream(): Flow<List<DataMenu>> =
        menuSource.menuStream()
}