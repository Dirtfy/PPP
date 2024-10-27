package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import android.util.Log
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.impl.common.firebase.FireStorePath
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.FireStoreRecord.Companion.convertToFireStoreRecord
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.FireStoreRecordDetail.Companion.convertToFireStoreRecordDetail
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.tagger.Tagger
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class RecordFireStore @Inject constructor(): RecordApi, Tagger {

    private val recordRef = Firebase.firestore.collection(FireStorePath.RECORD)

    override suspend fun create(
        record: DataRecord,
        detailList: List<DataRecordDetail>
    ): DataRecord {
        Firebase.firestore.runTransaction {
            val newRecord = recordRef.document()

            newRecord.set(
                record.convertToFireStoreRecord()
            )

            detailList.forEach {
                recordRef.document(newRecord.id)
                    .collection(FireStorePath.RECORD_DETAIL)
                    .document().set(it.convertToFireStoreRecordDetail())
            }
        }.await()

        return record
    }

    override suspend fun readAll(): List<DataRecord> {
        val recordSnapshot = recordRef.get().await()
        return readAll(recordSnapshot)
    }

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
        val query = recordRef
            .whereEqualTo("timestamp", Timestamp(Date(record.timestamp)))
        Log.d(TAG, "${Timestamp(Date(record.timestamp))}")
        val document = query.get().await().documents
        Log.d(TAG, "${record.timestamp}")

        val documentID = when(document.size) {
            1 -> document[0].id
            0 -> throw RecordException.NonExistQuery()
            else -> throw RecordException.NonUniqueQuery()
        }

        return recordRef.document(documentID)
            .collection(FireStorePath.RECORD_DETAIL).get().await()
            .documents.map { detailDocument ->
                detailDocument.toObject(FireStoreRecordDetail::class.java)!!
            }.map { recordDetail ->
                recordDetail.convertToDataRecordDetail()
            }
    }

    override fun recordStream(): Flow<List<DataRecord>> = callbackFlow {
        val recordSubscription = recordRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                val recordList = readAll(snapshot)
                trySend(recordList)
            } catch (e: Throwable) {
                // 혹시 모르니까 ㄹㅇㅋㅋ
            }
        }

        awaitClose {
            recordSubscription.remove()
        }
    }
}