package com.dirtfy.ppp.data.source.repository.account.record

import com.dirtfy.ppp.data.source.repository.account.RepositoryAccount

interface AccountRecordRepository {

    suspend fun create(record: RepositoryAccountRecord): RepositoryAccountRecord
    suspend fun readAll(): List<RepositoryAccountRecord>

}