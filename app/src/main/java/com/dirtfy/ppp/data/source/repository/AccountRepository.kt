package com.dirtfy.ppp.data.source.repository

import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.source.firestore.account.FireStoreAccount
import com.dirtfy.ppp.data.source.firestore.account.FireStoreAccountRecord
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun create(account: DataAccount): DataAccount
    suspend fun createRecord(
        accountNumber: Int,
        record: DataAccountRecord
    ): DataAccountRecord

    suspend fun readAllAccount(): List<DataAccount>
    suspend fun readAccountBalance(accountNumber: Int): Int
    suspend fun find(accountNumber: Int): DataAccount
    suspend fun readAllRecord(accountNumber: Int): List<DataAccountRecord>

    suspend fun update(account: DataAccount): DataAccount

    suspend fun isSameNumberExist(accountNumber: Int): Boolean
    suspend fun isNumberExist(accountNumber: Int): Boolean
    suspend fun getMaxAccountNumber(): Int

    fun accountStream(): Flow<List<DataAccount>>
    fun accountRecordStream(accountNumber: Int): Flow<List<DataAccountRecord>>
}