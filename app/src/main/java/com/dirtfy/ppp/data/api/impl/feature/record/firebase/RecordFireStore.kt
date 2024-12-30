package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import android.util.Log
import com.dirtfy.ppp.common.exception.ExternalException
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStorePath
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.FireStoreRecord.Companion.convertToFireStoreRecord
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.FireStoreRecordDetail.Companion.convertToFireStoreRecordDetail
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.tagger.Tagger
import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecordFireStore @Inject constructor(): RecordApi<Transaction>, Tagger {

    private val pathVersion = FireStorePath.Test

    private val recordRef = Firebase.firestore.collection(pathVersion.RECORD)
    private val recordIdRef = Firebase.firestore.document(pathVersion.RECORD_ID_COUNT)

    override suspend fun create(
        record: DataRecord,
        detailList: List<DataRecordDetail>
    ): DataRecord {
        if (record.id != DataRecord.ID_NOT_ASSIGNED)
            throw RecordException.IllegalIdAssignment()

        val createdRecord = Firebase.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(recordIdRef)
            val newRecordId = snapshot.getLong("count")!!.toInt()

            val newRecord = recordRef.document(newRecordId.toString())

            transaction.update(recordIdRef, "count", FieldValue.increment(1))

            val recordWithId = record.copy(id = newRecordId)
            transaction.set(newRecord, recordWithId.convertToFireStoreRecord())

            detailList.forEach {
                transaction.set(
                    newRecord.collection(pathVersion.RECORD_DETAIL).document(),
                    it.convertToFireStoreRecordDetail()
                )
            }

            recordWithId
        }.await()

        return createdRecord
    }

    override fun create(
        record: DataRecord,
        detailList: List<DataRecordDetail>,
        transaction: Transaction
    ): DataRecord {
        if (record.id != DataRecord.ID_NOT_ASSIGNED)
            throw RecordException.IllegalIdAssignment()

        val snapshot = transaction.get(recordIdRef)
        val newRecordId = snapshot.getLong("count")!!.toInt()

        val newRecord = recordRef.document(newRecordId.toString())

        transaction.update(recordIdRef, "count", FieldValue.increment(1))

        val recordWithId = record.copy(id = newRecordId)
        transaction.set(newRecord, recordWithId.convertToFireStoreRecord())

        detailList.forEach {
            transaction.set(
                newRecord.collection(pathVersion.RECORD_DETAIL).document(),
                it.convertToFireStoreRecordDetail()
            )
        }

        return recordWithId
    }

    override suspend fun read(id: Int): DataRecord {
        val recordSnapshot = recordRef.document(id.toString()).get().await()
        return recordSnapshot.toObject(FireStoreRecord::class.java)!!
            .convertToDataRecord()
    }

    override suspend fun readAll(): List<DataRecord> {
        val recordSnapshot = recordRef.get().await()
        return readAll(recordSnapshot)
    }

    override suspend fun <ValueType> readSumOf(
        key: String, value: ValueType, target: String
    ): Int {
        val query = recordRef.whereEqualTo(key, value)
        val aggregation = query.aggregate(AggregateField.sum(target))
        val result = aggregation.get(AggregateSource.SERVER).await()
        val receive = result.get(AggregateField.sum(target))

        Log.d(TAG, "$result\n$receive")

        return (receive as Long).toInt()
    }

    //TODO 이거 이러면 DB 데이터 전체를 메모리에 올리는거잖아 좀 위험하지 않나?
    private fun readAll(
        snapshot: QuerySnapshot
    ): List<DataRecord> {
        return snapshot.documents.map {
            it.toObject(FireStoreRecord::class.java)!!
        }.map { record ->
            record.convertToDataRecord()
        }
    }

    override suspend fun readDetail(record: DataRecord): List<DataRecordDetail> {
        val detailSnapshot = recordRef.document(record.id.toString())
            .collection(pathVersion.RECORD_DETAIL).get().await()
        if (detailSnapshot.metadata.isFromCache) Log.e(TAG, "detailSnapshot is from cache")
        return detailSnapshot
            .documents.map { detailDocument ->
                detailDocument.toObject(FireStoreRecordDetail::class.java)!!
            }.map { recordDetail ->
                recordDetail.convertToDataRecordDetail()
            }
    }

    override suspend fun update(record: DataRecord) {
        recordRef.document("${record.id}")
            .set(record.convertToFireStoreRecord())
            .await()
    }

    override suspend fun delete(id: Int): DataRecord {
        val docRef = recordRef.document("$id")
        Log.d(TAG, "$docRef-$id")
        val data = docRef.get().await()
            .toObject(FireStoreRecord::class.java)!!
            .convertToDataRecord()
        Log.d(TAG, "$data")
        docRef.delete().await()
        Log.d(TAG, "deleted")
        return data
    }

    override suspend fun getNextId(): Int {
        return recordIdRef.get().await()
            .getLong("count")!!.toInt() + 1
    }

    override fun recordStream(): Flow<List<DataRecord>> = callbackFlow {
        val recordSubscription = recordRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "recordStream snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "recordStream snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                val recordList = readAll(snapshot)
                trySend(recordList)
            } catch (e: Throwable) {
                Log.e(TAG, "record subscription\n${e.message}")
                close(e)
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }

    override suspend fun <ValueType> readRecordWith(
        key: String,
        value: ValueType
    ): List<DataRecord> {
        val targetRecordRef = recordRef
            .whereEqualTo(key, value)

        val querySnapshot = targetRecordRef.get().await()
        return readAll(querySnapshot)
    }

    override fun <ValueType> recordStreamWith(
        key: String,
        value: ValueType
    ): Flow<List<DataRecord>> = callbackFlow {
        val targetRecordRef = recordRef
            .whereEqualTo(key, value)

        val recordSubscription = targetRecordRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "recordStreamWith(key = $key, value = $value) - snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "recordStreamWith(key = $key, value = $value) - snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                val accountRecordList = readAll(snapshot)
                trySend(accountRecordList)
            } catch (e: Throwable) {
                Log.e(TAG, "recordStreamWith(key = $key, value = $value) - subscription fail\n${e.message}")
                close(e)
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }

    private fun <ValueType> readAllRecordWith(
        recordSnapshot: QuerySnapshot,
        key: String, value: ValueType
    ): List<DataRecord> {
        return recordSnapshot.documents.filter {
            it[key] == value
        }.map {
            it.toObject(FireStoreRecord::class.java)!!
        }.map {
            it.convertToDataRecord()
        }
    }

    override fun <ValueType, ReturnType> recordStreamSumOf(
        key: String,
        value: ValueType,
        target: String,
        sum: (List<DataRecord>) -> ReturnType
    ): Flow<ReturnType> = callbackFlow {
        val targetRecordRef = recordRef
            .whereEqualTo(key, value)

        val recordSubscription = targetRecordRef.addSnapshotListener { snapshot, error ->
            try {
                if (snapshot == null) {
                    Log.e(TAG, "recordStreamSumOf(key = $key, value = $value, target = $target) - snapshot null")
                    throw (error ?: ExternalException.ServerError())
                }
                if (snapshot.metadata.isFromCache) {
                    Log.e(TAG, "recordStreamSumOf(key = $key, value = $value, target = $target) - snapshot is from cache")
//                    throw ExternalException.NetworkError()
                }
                val result = sum(
                    readAllRecordWith(
                        snapshot,
                        key, value
                    )
                )

                trySend(result)
            } catch (e: Throwable) {
                Log.e(TAG, "recordStreamSumOf(key = $key, value = $value, target = $target) - subscription fail\n${e.message}")
                close(e)
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }
}