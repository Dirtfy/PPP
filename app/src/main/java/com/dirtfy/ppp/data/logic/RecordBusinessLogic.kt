package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordBusinessLogic @Inject constructor(
    private val repository: RecordApi
): BusinessLogic {

    fun readRecords() = operate {
        val recordList = repository.readAll()
            .sortedBy { -it.timestamp }
        recordList
    }

    fun readRecordDetail(record: DataRecord) = operate {
        val detailList = repository.readDetail(record) // TODO record timestamp로 하는게 나을듯?
        detailList
    }

    fun recordStream() = repository.recordStream()
        .map { it.sortedBy { data -> -data.timestamp } }
}