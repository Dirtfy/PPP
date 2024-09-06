package com.dirtfy.ppp.test.data.logic.impl

import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.test.data.logic.RecordLogic
import com.dirtfy.ppp.test.data.source.RecordSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordService @Inject constructor(
    private val recordSource: RecordSource
): RecordLogic {

    override fun readRecords() = flow<List<DataRecord>> {
        recordSource.readAll()
            .sortedBy { -it.timestamp }
    }

    override fun readRecordDetail(record: DataRecord) = flow<List<DataRecordDetail>> {
        recordSource.readDetail(record) // TODO record timestamp로 하는게 나을듯?
    }

    override fun recordStream() = recordSource.recordStream()
}