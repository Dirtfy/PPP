package com.dirtfy.ppp.model.accounting.managing

import com.dirtfy.ppp.common.Util.convertToCalendar
import com.dirtfy.ppp.common.Util.convertToLong
import com.dirtfy.ppp.common.Util.convertToTimestamp
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
                timestamp = data.timestamp.convertToTimestamp(),
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
                timestamp = _accountRecord.timestamp!!.convertToLong(),
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
        filter: (AccountRecord) -> AccountRecord
    ) {
        repositoryRef.get().await().documents.forEach { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot == null) return@forEach

            val _accountRecord = documentSnapshot.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecord(
                recordID = documentSnapshot.id,
                timestamp = _accountRecord.timestamp!!.convertToLong(),
                accountNumber = _accountRecord.accountNumber!!,
                userName = _accountRecord.userName!!,
                amount = _accountRecord.amount!!,
                result = _accountRecord.result!!
            )

            val updatedData = filter(accountRecord)

            repositoryRef.document(documentSnapshot.id).set(
                _AccountRecordData(
                    timestamp = updatedData.timestamp.convertToTimestamp(),
                    accountNumber = updatedData.accountNumber,
                    userName = updatedData.userName,
                    amount = updatedData.amount,
                    result = updatedData.result
                )
            )
        }
    }

    override suspend fun delete(
        filter: (AccountRecord) -> Boolean
    ) {
        repositoryRef.get().await().documents.forEach {
            val _accountRecord = it.toObject<_AccountRecordData>()!!
            val accountRecord = AccountRecord(
                recordID = it.id,
                timestamp = _accountRecord.timestamp!!.convertToLong(),
                accountNumber = _accountRecord.accountNumber!!,
                userName = _accountRecord.userName!!,
                amount = _accountRecord.amount!!,
                result = _accountRecord.result!!
            )

            if (!filter(accountRecord)) return@forEach

            repositoryRef.document(it.id).delete().await()
        }
    }

}