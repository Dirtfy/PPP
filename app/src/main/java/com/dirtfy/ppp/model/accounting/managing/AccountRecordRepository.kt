package com.dirtfy.ppp.model.accounting.managing

import com.dirtfy.ppp.model.Repository
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object AccountRecordRepository: Repository<AccountRecordData> {

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT_RECORD)

    override suspend fun create(data: AccountRecordData): AccountRecordData {
        val newAccountRecordRef = repositoryRef.document()

        newAccountRecordRef.set(
            _AccountRecordData(
                timestamp = data.timestamp,
                accountNumber = data.accountNumber,
                userName = data.userName,
                amount = data.amount,
                result = data.result
            )
        )

        return data.copy(recordID = newAccountRecordRef.id)
    }

    override suspend fun read(filter: (AccountRecordData) -> Boolean): List<AccountRecordData> {
        val recordList = mutableListOf<AccountRecordData>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _accountRecord = documentSnapshot.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecordData(
                recordID = documentSnapshot.id,
                timestamp = _accountRecord.timestamp!!,
                accountNumber = _accountRecord.accountNumber!!,
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

}