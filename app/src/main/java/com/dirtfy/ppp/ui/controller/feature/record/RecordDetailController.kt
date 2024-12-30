package com.dirtfy.ppp.ui.controller.feature.record

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import kotlinx.coroutines.flow.Flow

interface RecordDetailController {

    val screenData: Flow<UiRecordDetailScreenState>

    suspend fun updateRecordType(type: String)
    suspend fun updateRecordDetailList()
    suspend fun updateNowRecord(record: UiRecord)
    suspend fun deleteRecord(record: UiRecord)
    fun setRecordDetailListState(state: UiScreenState)
    fun setNowRecordState(state: UiScreenState)
}