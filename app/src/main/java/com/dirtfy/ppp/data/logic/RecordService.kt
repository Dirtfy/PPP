package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.source.repository.RecordRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordService @Inject constructor(
    private val repository: RecordRepository
): Service {

    fun readRecords() = flow {
        val recordList = repository.readAll()
            .sortedBy { -it.timestamp }
        emit(recordList)
    }

    fun readRecordDetail(record: DataRecord) = flow {
        val detailList = repository.readDetail(record) // TODO record timestamp로 하는게 나을듯?
        emit(detailList)
    }

    fun recordStream() = repository.recordStream()
        .map { it.sortedBy { data -> -data.timestamp } }
}