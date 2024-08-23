package com.dirtfy.ppp.data.source.repository

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail

interface RecordRepository {

    suspend fun create(record: DataRecord, detailList: List<DataRecordDetail>): DataRecord
    suspend fun readAll(): List<DataRecord>
    suspend fun readDetail(record: DataRecord): List<DataRecordDetail>

}