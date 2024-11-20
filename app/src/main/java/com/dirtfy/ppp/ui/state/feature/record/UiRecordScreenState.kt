package com.dirtfy.ppp.ui.state.feature.record

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode

data class UiRecordScreenState(
    val recordList: List<UiRecord> = emptyList(),
    val recordDetailList: List<UiRecordDetail> = emptyList(),
    val searchClue: String = "",
    val nowRecord: UiRecord = UiRecord(),
    val mode: UiRecordMode = UiRecordMode.Main,

    val recordListState: UiScreenState = UiScreenState(),
    val recordDetailListState: UiScreenState = UiScreenState(),
    val nowRecordState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
