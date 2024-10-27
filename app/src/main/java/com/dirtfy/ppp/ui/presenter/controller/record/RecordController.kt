package com.dirtfy.ppp.ui.presenter.controller.record

import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordMode
import com.dirtfy.ppp.ui.dto.record.screen.UiRecordScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface RecordController: Controller<UiRecordScreenState, RecordController> {

    @Deprecated("screen state synchronized with repository")
    suspend fun updateRecordList()
    fun updateSearchClue(clue: String)
    fun updateNowRecord(record: UiRecord)
    fun setMode(mode: UiRecordMode)

}