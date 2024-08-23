package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.source.repository.RecordRepository

class RecordService(
    val repository: RecordRepository
): Service {

    fun readRecords() = asFlow {
        repository.readAll()
    }

    fun readRecordDetail(record: DataRecord) = asFlow {
        repository.readDetail(record) // TODO record timestamp로 하는게 나을듯?
    }
}