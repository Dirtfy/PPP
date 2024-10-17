package com.dirtfy.ppp.ui.presenter.controller.record

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordMode
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface RecordController: Controller {

    val recordList: StateFlow<FlowState<List<UiRecord>>>

    val searchClue: StateFlow<String>
    val nowRecord: StateFlow<UiRecord>
    val mode: StateFlow<UiRecordMode>

    fun updateRecordList()
    fun updateSearchClue(clue: String)
    fun updateNowRecord(record: UiRecord)
    fun setMode(mode: UiRecordMode)

}