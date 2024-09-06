package com.dirtfy.ppp.test.data.logic

import com.dirtfy.ppp.data.dto.DataMenu
import kotlinx.coroutines.flow.Flow

interface MenuLogic {

    fun createMenu(menu: DataMenu): Flow<DataMenu>

    fun readMenu(): Flow<List<DataMenu>>

    fun deleteMenu(menu: DataMenu): Flow<DataMenu>

    fun menuStream(): Flow<List<DataMenu>>
}