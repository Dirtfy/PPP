package com.dirtfy.ppp.ui.controller.feature.record

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode

interface RecordController: Controller<UiRecordScreenState, RecordController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateRecordList()
    fun retryUpdateRecordList()
    suspend fun updateRecordType(type: String)
    suspend fun updateRecordDetailList()
    fun updateDateRange(start: Long?, end: Long?)
    suspend fun updateNowRecord(record: UiRecord)
    suspend fun deleteRecord(record: UiRecord)
    fun setMode(mode: UiRecordMode)
    fun setRecordListState(state: UiScreenState)
    fun setRecordDetailListState(state: UiScreenState)
    fun setNowRecordState(state: UiScreenState)
}