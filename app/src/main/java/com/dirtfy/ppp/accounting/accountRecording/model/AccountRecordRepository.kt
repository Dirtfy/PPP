package com.dirtfy.ppp.accounting.accountRecording.model

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

object AccountRecordRepository: Repository<AccountRecordData> {

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT_RECORD)

    override suspend fun create(data: AccountRecordData): AccountRecordData {
        val newAccountRecordRef = repositoryRef.document()

        newAccountRecordRef.set(
            _AccountRecordData(
                timestamp = data.timestamp,
                accountID = data.accountID,
                userName = data.userName,
                amount = data.amount,
                result = data.result
            )
        )

        return AccountRecordData(
            recordID = newAccountRecordRef.id,
            timestamp = data.timestamp,
            accountID = data.accountID,
            userName = data.userName,
            amount = data.amount,
            result = data.result
        )
    }

    override suspend fun read(filter: (AccountRecordData) -> Boolean): List<AccountRecordData> {
        val recordList = mutableListOf<AccountRecordData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _accountRecord = documentSnapshot.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecordData(
                recordID = documentSnapshot.id,
                timestamp = _accountRecord.timestamp!!,
                accountID = _accountRecord.accountID!!,
                userName = _accountRecord.userName!!,
                amount = _accountRecord.amount!!,
                result = _accountRecord.result!!
            )

            if (!filter(accountRecord)) return@forEach

            recordList.add(accountRecord)
        }

        return recordList
    }

    override suspend fun update(filter: (AccountRecordData) -> AccountRecordData) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(filter: (AccountRecordData) -> Boolean) {
        TODO("Not yet implemented")
    }

    suspend fun readAll(): List<AccountRecordData> {
        val recordList = mutableListOf<AccountRecordData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _accountRecord = documentSnapshot.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecordData(
                recordID = documentSnapshot.id,
                timestamp = _accountRecord.timestamp!!,
                accountID = _accountRecord.accountID!!,
                userName = _accountRecord.userName!!,
                amount = _accountRecord.amount!!,
                result = _accountRecord.result!!
            )


            recordList.add(accountRecord)
        }

        return recordList
    }
}