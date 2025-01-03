package com.dirtfy.ppp.data.api.impl.feature.account.firebase

import android.util.Log
import com.dirtfy.ppp.common.exception.ExternalException
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStorePath
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.FireStoreAccount.Companion.convertToFireStoreAccount
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.FireStoreRecord
import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.tagger.Tagger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class AccountFireStore @Inject constructor(): AccountApi, Tagger {

    private val pathVersion = FireStorePath.Service

    private val accountRef = Firebase.firestore.collection(pathVersion.ACCOUNT)
    private val recordRef = Firebase.firestore.collection(pathVersion.RECORD)

    override suspend fun create(account: DataAccount): DataAccount {
        Firebase.firestore.runTransaction {
            val newAccount = accountRef.document()

            newAccount.set(
                account.convertToFireStoreAccount()
            )
        }.await()

        return account
    }

    private suspend fun getNextRecordId(): Int {
        return Firebase.firestore.document(pathVersion.RECORD_ID_COUNT).get().await()
            .getLong("count")!!.toInt() + 1
    }

    @Deprecated("moved to record api")
    override suspend fun createRecord(
        accountNumber: Int,
        record: DataAccountRecord
    ): DataAccountRecord {
        val nextRecordId = getNextRecordId()

        Firebase.firestore.runTransaction {
            val newRecord = recordRef.document()

            Log.d(TAG, "$record")

            Firebase.firestore.document(pathVersion.RECORD_ID_COUNT)
                .update("count", FieldValue.increment(1))

            newRecord.set(
                FireStoreRecord(
                    id = nextRecordId,
                    timestamp = Timestamp(Date(record.timestamp)),
                    income = record.difference,
                    type = accountNumber.toString(),
                    issuedBy = record.issuedName,
                )
            )
        }.await()

        return record
    }

    override suspend fun readAllAccount(): List<DataAccount> {
        return readAllAccount(accountRef.get().await())
    }

    @Deprecated("moved to record api")
    override suspend fun readAccountBalance(accountNumber: Int): Int {
        val query = recordRef.whereEqualTo("type", "$accountNumber")
        val aggregation = query.aggregate(AggregateField.sum("income"))
        val result = aggregation.get(AggregateSource.SERVER).await()
        val value = result.get(AggregateField.sum("income"))

        Log.d(TAG, "$result\n$value")

        return (value as Long).toInt()
    }

    override suspend fun find(accountNumber: Int): DataAccount {
        val query = accountRef.whereEqualTo("number", accountNumber)
        val document = query.get().await().documents[0]

        return document.toObject(FireStoreAccount::class.java)!!
            .convertToDataAccount()
    }

    @Deprecated("moved to record api")
    override suspend fun readAllRecord(accountNumber: Int): List<DataAccountRecord> {
        val query = recordRef
            .whereEqualTo("type", "$accountNumber")
            .orderBy("timestamp")
        val querySnapshot = query.get().await()

        return readAllRecord(querySnapshot)
    }

    override suspend fun update(account: DataAccount): DataAccount {
        val query = accountRef.whereEqualTo("number", account.number)
        val document = query.get().await().documents[0]

        Firebase.firestore.runTransaction {
            accountRef.document(document.id)
                .set(account.convertToFireStoreAccount())
        }.await()

        return account
    }

    override suspend fun isSameNumberExist(accountNumber: Int): Boolean {
        val query = accountRef.whereEqualTo("number", accountNumber)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size > 0
    }

    override suspend fun isNumberExist(accountNumber: Int): Boolean {
        val query = accountRef.whereEqualTo("number", accountNumber)
        val matchedDocumentList = query.get().await().documents

        return matchedDocumentList.size >= 1
    }

    override suspend fun getMaxAccountNumber(): Int {
        return Firebase.firestore.document(pathVersion.MAX_ACCOUNT_NUMBER).get().await()
            .getLong("number")!!.toInt()
    }

    override fun accountStream(): Flow<List<DataAccount>> = callbackFlow {
        val accountSubscription = accountRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "accountStream snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "accountStream snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                val accountList = readAllAccount(snapshot)
                trySend(accountList)
            } catch (e: Throwable) {
                Log.e(TAG, "account subscription fail\n${e.message}")
                close(e)
            }
        }

        awaitClose {
            accountSubscription.remove()
        }
    }

    @Deprecated("moved to record api")
    override fun accountRecordStream(accountNumber: Int): Flow<List<DataAccountRecord>> = callbackFlow {
        val targetRecordRef = recordRef
            .whereEqualTo("type", "$accountNumber")
            .orderBy("timestamp")

        val recordSubscription = targetRecordRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val accountRecordList = readAllRecord(snapshot)
                trySend(accountRecordList)
            } catch (e: Throwable) {
                Log.e(TAG, "record subscription fail\n${e.message}")
                throw e
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }

    private fun readAllAccount(
        accountSnapshot: QuerySnapshot
    ): List<DataAccount> {
        return accountSnapshot.documents.map {
            it.toObject(FireStoreAccount::class.java)!!
        }.map { account ->
            account.convertToDataAccount()
        }
    }

    private fun readAllRecord(
        recordSnapshot: QuerySnapshot
    ): List<DataAccountRecord> {
        var result = 0
        return recordSnapshot.documents.map {
            it.toObject(FireStoreRecord::class.java)!!
        }.map {
            result += it.income ?: throw RecordException.DifferenceLoss()
            it.convertToDataAccountRecord(result = result)
        }
    }
}