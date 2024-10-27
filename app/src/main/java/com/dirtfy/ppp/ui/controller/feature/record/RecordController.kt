package com.dirtfy.ppp.ui.controller.feature.record

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.record.UiRecordScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode

interface RecordController: Controller<UiRecordScreenState, RecordController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateRecordList()
    fun updateSearchClue(clue: String)
    fun updateNowRecord(record: UiRecord)
    fun setMode(mode: UiRecordMode)

}