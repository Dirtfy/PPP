package com.dirtfy.ppp.ui.state.feature.record

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail

data class UiRecordDetailScreenState(
    val recordDetailList: List<UiRecordDetail> = emptyList(),
    val nowRecord: UiRecord = UiRecord(),

    val recordDetailListState: UiScreenState = UiScreenState()
)
