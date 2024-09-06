package com.dirtfy.ppp.test.ui.presenter.contract.record

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiRecord
import com.dirtfy.ppp.ui.dto.UiRecordDetail
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface RecordDetailController: Controller {

    val recordDetailList: StateFlow<FlowState<List<UiRecordDetail>>>

    val nowRecord: StateFlow<UiRecord>

    fun updateRecordDetailList(record: UiRecord)
    fun updateNowRecord(record: UiRecord)

}