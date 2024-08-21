package com.dirtfy.ppp.data.source.repository.menu

interface MenuRepository {

    suspend fun create(menu: RepositoryMenu): RepositoryMenu
    suspend fun readAll(): List<RepositoryMenu>
    suspend fun delete(menu: RepositoryMenu): RepositoryMenu

    suspend fun isSameNameExist(name: String): Boolean
}