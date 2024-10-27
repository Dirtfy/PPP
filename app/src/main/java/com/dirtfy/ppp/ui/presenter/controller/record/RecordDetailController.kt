package com.dirtfy.ppp.ui.presenter.controller.record

import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.screen.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface RecordDetailController: Controller {

    val screenData: StateFlow<UiRecordDetailScreenState>

    suspend fun updateRecordDetailList(record: UiRecord)
    fun updateNowRecord(record: UiRecord)

    fun request(job: suspend RecordDetailController.() -> Unit)

}