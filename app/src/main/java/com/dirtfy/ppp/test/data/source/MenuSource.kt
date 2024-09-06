package com.dirtfy.ppp.test.data.source

import com.dirtfy.ppp.data.dto.DataMenu
import kotlinx.coroutines.flow.Flow

interface MenuSource {

    suspend fun create(menu: DataMenu): DataMenu
    suspend fun readAll(): List<DataMenu>
    suspend fun delete(menu: DataMenu): DataMenu

    fun menuStream(): Flow<List<DataMenu>>

    suspend fun isSameNameExist(name: String): Boolean
}