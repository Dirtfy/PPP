package com.dirtfy.ppp.data.api

import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import kotlinx.coroutines.flow.Flow

interface RecordApi {

    suspend fun create(record: DataRecord, detailList: List<DataRecordDetail>): DataRecord
    suspend fun readAll(): List<DataRecord>
    suspend fun readDetail(record: DataRecord): List<DataRecordDetail>

    fun recordStream(): Flow<List<DataRecord>>

}