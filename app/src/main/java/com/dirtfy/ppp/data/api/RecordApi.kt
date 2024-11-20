package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import kotlinx.coroutines.flow.Flow

interface RecordApi {

    suspend fun create(record: DataRecord, detailList: List<DataRecordDetail>): DataRecord
    suspend fun read(id: Int): DataRecord
    suspend fun readAll(): List<DataRecord>
    suspend fun <ValueType> readSumOf(key: String, value: ValueType, target:String): Int
    suspend fun readDetail(record: DataRecord): List<DataRecordDetail>

    suspend fun getNextId(): Int

    fun recordStream(): Flow<List<DataRecord>>
    fun <ValueType> recordStreamWith(key: String, value: ValueType): Flow<List<DataRecord>>
    fun <ValueType, ReturnType> recordStreamSumOf(
        key: String, value: ValueType, target:String, sum: (List<DataRecord>) -> ReturnType
    ): Flow<ReturnType>

}