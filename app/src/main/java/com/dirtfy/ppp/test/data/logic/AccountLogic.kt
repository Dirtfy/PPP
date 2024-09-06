package com.dirtfy.ppp.test.data.logic

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.data.dto.DataAccountRecord
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

interface AccountLogic {

    fun createAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ): Flow<DataAccount>

    fun createAccountNumber(): Flow<Int>

    fun readAllAccounts(): Flow<List<DataAccount>>

    fun updateAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ): Flow<DataAccount>

    fun readAccountRecord(accountNumber: Int): Flow<List<DataAccountRecord>>

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ): Flow<DataAccountRecord>

    fun accountStream(): Flow<List<DataAccount>>
    fun accountRecordStream(accountNumber: Int): Flow<List<DataAccountRecord>>
}