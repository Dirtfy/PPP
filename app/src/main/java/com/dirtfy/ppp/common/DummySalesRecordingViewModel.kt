package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract

object DummySalesRecordingViewModel: SalesRecordingViewModelContract.API {
    override val recordList: State<List<SalesRecordingViewModelContract.DTO.Record>>
        get() = TODO("Not yet implemented")
    override val selectedRecord: State<SalesRecordingViewModelContract.DTO.Record?>
        get() = TODO("Not yet implemented")

    override fun checkSalesRecordList() {
        TODO("Not yet implemented")
    }

    override fun clickRecord(record: SalesRecordingViewModelContract.DTO.Record) {
        TODO("Not yet implemented")
    }

    override val recordDetail: State<List<SalesRecordingViewModelContract.DTO.Menu>>
        get() = TODO("Not yet implemented")

    override fun checkRecordDetail() {
        TODO("Not yet implemented")
    }
}