package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.source.repository.RecordRepository
import javax.inject.Inject

class RecordService @Inject constructor(
    private val repository: RecordRepository
): Service {

    fun readRecords() = asFlow {
        repository.readAll()
            .sortedBy { -it.timestamp }
    }

    fun readRecordDetail(record: DataRecord) = asFlow {
        repository.readDetail(record) // TODO record timestamp로 하는게 나을듯?
    }
}