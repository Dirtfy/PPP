package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import kotlinx.coroutines.flow.Flow

interface AccountApi {

    suspend fun create(account: DataAccount): DataAccount
    @Deprecated("moved to record api")
    suspend fun createRecord(
        accountNumber: Int,
        record: DataAccountRecord
    ): DataAccountRecord

    suspend fun readAllAccount(): List<DataAccount>
    @Deprecated("moved to record api")
    suspend fun readAccountBalance(accountNumber: Int): Int
    suspend fun find(accountNumber: Int): DataAccount
    @Deprecated("moved to record api")
    suspend fun readAllRecord(accountNumber: Int): List<DataAccountRecord>

    suspend fun update(account: DataAccount): DataAccount

    suspend fun isSameNumberExist(accountNumber: Int): Boolean
    suspend fun isNumberExist(accountNumber: Int): Boolean
    suspend fun getMaxAccountNumber(): Int

    fun accountStream(): Flow<List<DataAccount>>
    @Deprecated("moved to record api")
    fun accountRecordStream(accountNumber: Int): Flow<List<DataAccountRecord>>
}