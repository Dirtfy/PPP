package com.dirtfy.ppp.test.data.logic

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import kotlinx.coroutines.flow.Flow

interface RecordLogic {

    fun readRecords(): Flow<List<DataRecord>>

    fun readRecordDetail(record: DataRecord): Flow<List<DataRecordDetail>>

    fun recordStream(): Flow<List<DataRecord>>
}