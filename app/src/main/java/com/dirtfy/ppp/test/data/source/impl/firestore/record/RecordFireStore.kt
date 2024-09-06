package com.dirtfy.ppp.test.data.source.impl.firestore.record

import android.util.Log
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecord
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecord.Companion.convertToFireStoreRecord
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecordDetail
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecordDetail.Companion.convertToFireStoreRecordDetail
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.test.data.source.RecordSource
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

class RecordFireStore @Inject constructor(
): RecordSource, Tagger {

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

    private fun readAll(
        recordSnapshot: QuerySnapshot
    ): List<DataRecord> {
        return recordSnapshot.documents.map { document ->
            document.toObject(FireStoreRecord::class.java)!!
        }.map { record ->
            Log.d(TAG, "read: $record: ${record.timestamp}")
            val v = record.convertToDataRecord()
            Log.d(TAG, "read: $v: ${v.timestamp}")
            v
        }
    }
    override suspend fun readAll(): List<DataRecord> {
        val recordSnapshot = recordRef.get().await()

        return readAll(recordSnapshot)
    }

    private fun readDetail(
        recordSnapshot: QuerySnapshot
    ): List<DataRecordDetail> {
        return recordSnapshot.documents
            .map { detailDocument ->
                detailDocument.toObject(FireStoreRecordDetail::class.java)!!
            }.map { recordDetail ->
                recordDetail.convertToDataRecordDetail()
            }
    }
    override suspend fun readDetail(record: DataRecord): List<DataRecordDetail> {
        val query = recordRef
            .whereEqualTo("timestamp", Timestamp(Date(record.timestamp)))
        val document = query.get().await().documents
        Log.d(TAG, "${record.timestamp}")

        val documentID = when(document.size) {
            1 -> document[0].id
            0 -> throw RecordException.NonExistQuery()
            else -> throw RecordException.NonUniqueQuery()
        }

        val recordSnapshot = recordRef.document(documentID)
            .collection(FireStorePath.RECORD_DETAIL).get().await()

        return readDetail(recordSnapshot)
    }

    override fun recordStream(): Flow<List<DataRecord>>
    = callbackFlow {
        val subscription = recordRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends events to the flow! Consumers will get the new events
            try {
                val recordList = readAll(snapshot)

                trySend(recordList)
            } catch (error: Throwable) {
                // Event couldn't be sent to the flow
            }
        }

        awaitClose { subscription.remove() }
    }
}