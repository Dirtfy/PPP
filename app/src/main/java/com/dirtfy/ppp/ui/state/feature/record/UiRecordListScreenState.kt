package com.dirtfy.ppp.ui.state.feature.record

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord

data class UiRecordListScreenState(
    val recordList: List<UiRecord> = emptyList(),
    val searchClue: String = "",

    val recordListState: UiScreenState = UiScreenState(),
)
