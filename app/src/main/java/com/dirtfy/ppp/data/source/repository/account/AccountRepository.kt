package com.dirtfy.ppp.data.source.repository.account

interface AccountRepository {

    suspend fun create(account: RepositoryAccount): RepositoryAccount
    suspend fun readAll(): List<RepositoryAccount>
    suspend fun find(accountNumber: Int): RepositoryAccount
    suspend fun update(account: RepositoryAccount): RepositoryAccount

    suspend fun isSameNumberExist(accountNumber: Int): Boolean
    suspend fun isNumberExist(accountNumber: Int): Boolean
    suspend fun getMaxAccountNumber(): Int
}