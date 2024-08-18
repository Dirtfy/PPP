package com.dirtfy.ppp.model.accounting.managing

import com.dirtfy.ppp.contract.model.accounting.AccountRecordModelContract
import com.dirtfy.ppp.contract.model.accounting.AccountRecordModelContract.DTO.AccountRecord
import com.dirtfy.ppp.model.RepositoryPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object AccountRecordRepository: AccountRecordModelContract.API {

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.ACCOUNT_RECORD)

    override suspend fun create(
        data: AccountRecord
    ): AccountRecord {
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

    override suspend fun read(
        filter: (AccountRecord) -> Boolean
    ): List<AccountRecord> {
        val recordList = mutableListOf<AccountRecord>()

        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _accountRecord = documentSnapshot.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecord(
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

    override suspend fun update(
        filter: (AccountRecord)
        -> AccountRecord
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        filter: (AccountRecord) -> Boolean
    ) {
        TODO("Not yet implemented")
    }

}