package com.dirtfy.ppp.data.source.firestore

import com.dirtfy.ppp.data.source.repository.account.record.AccountRecordRepository
import com.dirtfy.ppp.data.source.repository.account.record.RepositoryAccountRecord
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AccountRecordFireStore: AccountRecordRepository {

    private val ref = Firebase.firestore.collection(FireStorePath.ACCOUNT_RECORD)

    override suspend fun create(record: RepositoryAccountRecord): RepositoryAccountRecord {
        val newRecord = ref.document()

        newRecord.set(record).await()

        return record
    }

    override suspend fun readAll(): List<RepositoryAccountRecord> {
        return ref.get().await().documents.map {
            it.toObject(RepositoryAccountRecord::class.java)!!
        }
    }
}