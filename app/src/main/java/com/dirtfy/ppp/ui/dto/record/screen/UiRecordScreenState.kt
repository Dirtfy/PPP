package com.dirtfy.ppp.ui.dto.record.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordMode

data class UiRecordScreenState(
    val recordList: List<UiRecord> = emptyList(),
    val searchClue: String = "",
    val nowRecord: UiRecord = UiRecord(),
    val mode: UiRecordMode = UiRecordMode.Main,

    val recordListState: UiScreenState = UiScreenState(),
    val nowRecordState: UiScreenState = UiScreenState(UiState.COMPLETE)
)
