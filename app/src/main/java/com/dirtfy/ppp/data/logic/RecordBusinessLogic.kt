package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordBusinessLogic @Inject constructor(
    private val repository: RecordApi
): BusinessLogic {

    fun readRecord(id: Int) = operate {
        repository.read(id)
    }

    fun readRecords() = operate {
        val recordList = repository.readAll()
            .sortedBy { -it.timestamp }
        recordList
    }

    fun readRecordDetail(record: DataRecord) = operate {
        val detailList = repository.readDetail(record)
        detailList
    }

    fun recordStream() = repository.recordStream()
        .map { it.sortedBy { data -> -data.timestamp } }
}