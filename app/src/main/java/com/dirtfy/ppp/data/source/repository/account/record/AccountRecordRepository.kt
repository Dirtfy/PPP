package com.dirtfy.ppp.data.source.repository.account.record

interface AccountRecordRepository {

    suspend fun create(record: RepositoryAccountRecord): RepositoryAccountRecord
    suspend fun readAll(): List<RepositoryAccountRecord>

}