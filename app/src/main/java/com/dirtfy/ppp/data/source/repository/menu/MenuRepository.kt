package com.dirtfy.ppp.data.source.repository.menu

import com.dirtfy.ppp.common.FlowState
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun create(menu: RepositoryMenu): RepositoryMenu
    suspend fun readAll(): List<RepositoryMenu>
    suspend fun delete(menu: RepositoryMenu): RepositoryMenu

    suspend fun isSameNameExist(name: String): Boolean
}