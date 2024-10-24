package com.dirtfy.ppp.data.source.firestore.record

import android.util.Log
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.source.firestore.FireStorePath
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecord.Companion.convertToFireStoreRecord
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecordDetail.Companion.convertToFireStoreRecordDetail
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.tagger.Tagger
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class RecordFireStore @Inject constructor(): RecordRepository, Tagger {

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
        return recordRef.get().await().documents.map { document ->
            document.toObject(FireStoreRecord::class.java)!!
        }.map { record ->
            Log.d(TAG, "read: $record: ${record.timestamp}")
            val v = record.convertToDataRecord()
            Log.d(TAG, "read: $v: ${v.timestamp}")
            v
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
}