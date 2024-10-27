package com.dirtfy.ppp.ui.dto.record.screen

import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordDetail

data class UiRecordDetailScreenState(
    val recordDetailList: List<UiRecordDetail> = emptyList(),
    val nowRecord: UiRecord = UiRecord(),

    val recordDetailListState: UiScreenState = UiScreenState()
)
