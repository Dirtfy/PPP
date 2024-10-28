package com.dirtfy.ppp.ui.presenter.controller.record

import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.screen.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller

interface RecordDetailController
    : Controller<UiRecordDetailScreenState, RecordDetailController> {

    suspend fun updateRecordDetailList(record: UiRecord)
    suspend fun updateNowRecord(record: UiRecord)

}