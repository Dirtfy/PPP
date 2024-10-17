package com.dirtfy.ppp.data.source.repository

import com.dirtfy.ppp.data.dto.DataMenu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun create(menu: DataMenu): DataMenu
    suspend fun readAll(): List<DataMenu>
    suspend fun delete(menu: DataMenu): DataMenu

    suspend fun isSameNameExist(name: String): Boolean

    fun menuStream(): Flow<List<DataMenu>>
}