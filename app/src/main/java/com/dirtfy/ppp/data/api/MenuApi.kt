package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import kotlinx.coroutines.flow.Flow

interface MenuApi {

    suspend fun create(menu: DataMenu): DataMenu
    suspend fun readAll(): List<DataMenu>
    suspend fun delete(menu: DataMenu): DataMenu

    suspend fun isSameNameExist(name: String): Boolean

    fun menuStream(): Flow<List<DataMenu>>
}