package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import android.util.Log
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
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecordFireStore @Inject constructor(): RecordApi, Tagger {

    private val recordRef = Firebase.firestore.collection(FireStorePath.RECORD)

    override suspend fun create(
        record: DataRecord,
        detailList: List<DataRecordDetail>
    ): DataRecord {
        Firebase.firestore.runTransaction {
            val newRecord = recordRef.document(record.id.toString())

            Firebase.firestore.document(FireStorePath.RECORD_ID_COUNT)
                .update("count", FieldValue.increment(1))

            newRecord.set(
                record.convertToFireStoreRecord()
            )

            detailList.forEach {
                recordRef.document(newRecord.id)
                    .collection(FireStorePath.RECORD_DETAIL)
                    .document().set(it.convertToFireStoreRecordDetail())
            }
        }.await()

        val currentId = getNextId()-1

        return record.copy(id = currentId)
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
        return recordRef.document(record.id.toString())
            .collection(FireStorePath.RECORD_DETAIL).get().await()
            .documents.map { detailDocument ->
                detailDocument.toObject(FireStoreRecordDetail::class.java)!!
            }.map { recordDetail ->
                recordDetail.convertToDataRecordDetail()
            }
    }

    override suspend fun getNextId(): Int {
        return Firebase.firestore.document(FireStorePath.RECORD_ID_COUNT).get().await()
            .getLong("count")!!.toInt() + 1
    }

    override fun recordStream(): Flow<List<DataRecord>> = callbackFlow {
        val recordSubscription = recordRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val recordList = readAll(snapshot)
                trySend(recordList)
            } catch (e: Throwable) {
                Log.e(TAG, "record subscription\n${e.message}")
                throw e
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }

    override fun <ValueType> recordStreamWith(
        key: String,
        value: ValueType
    ): Flow<List<DataRecord>> = callbackFlow {
        val targetRecordRef = recordRef
            .whereEqualTo(key, value)

        val recordSubscription = targetRecordRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val accountRecordList = readAll(snapshot)
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
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val result = sum(
                    readAllRecordWith(
                        snapshot,
                        key, value
                    )
                )

                trySend(result)
            } catch (e: Throwable) {
                Log.e(TAG, "record subscription fail\n${e.message}")
                throw e
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }
}