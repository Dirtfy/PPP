package com.dirtfy.ppp.ui.presenter.viewmodel.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.RecordService
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecord
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.ui.dto.UiRecord
import com.dirtfy.ppp.ui.dto.UiRecordDetail
import com.dirtfy.ppp.ui.dto.UiRecordDetail.Companion.convertToUiRecordDetail
import com.dirtfy.ppp.ui.presenter.controller.record.RecordDetailController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class RecordDetailViewModel: ViewModel(), RecordDetailController {

    private val recordService: RecordService = RecordService(RecordFireStore())

    private val _recordDetailList: MutableStateFlow<FlowState<List<UiRecordDetail>>>
    = MutableStateFlow(FlowState.loading())
    override val recordDetailList: StateFlow<FlowState<List<UiRecordDetail>>>
        get() = _recordDetailList

    suspend fun _updateRecordDetailList(record: UiRecord) {
        recordService.readRecordDetail(record.convertToDataRecord())
            .conflate().collect {
                _recordDetailList.value = it.passMap { data ->
                    data.map { recordDetail ->
                        recordDetail.convertToUiRecordDetail()
                    }
                }
            }
    }
    override fun updateRecordDetailList(record: UiRecord) = request {
        _updateRecordDetailList(record)
    }

    fun request(job: suspend RecordDetailViewModel.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}