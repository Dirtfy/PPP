package com.dirtfy.ppp.ui.controller.feature.record

import com.dirtfy.ppp.ui.controller.common.Controller
import com.dirtfy.ppp.ui.state.feature.record.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord

interface RecordDetailController
    : Controller<UiRecordDetailScreenState, RecordDetailController> {

    suspend fun updateRecordDetailList(record: UiRecord)
    fun updateNowRecord(record: UiRecord)

}