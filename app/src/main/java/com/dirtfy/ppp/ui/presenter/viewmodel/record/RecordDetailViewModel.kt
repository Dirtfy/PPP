package com.dirtfy.ppp.ui.presenter.viewmodel.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.logic.RecordService
import com.dirtfy.ppp.data.source.firestore.record.FireStoreRecord
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.ui.dto.UiRecord
import com.dirtfy.ppp.ui.dto.UiRecordDetail
import com.dirtfy.ppp.ui.dto.UiRecordDetail.Companion.convertToUiRecordDetail
import com.dirtfy.ppp.ui.presenter.controller.record.RecordDetailController
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val recordService: RecordService
): ViewModel(), RecordDetailController, Tagger {

    private val _recordDetailList: MutableStateFlow<FlowState<List<UiRecordDetail>>>
    = MutableStateFlow(FlowState.loading())
    override val recordDetailList: StateFlow<FlowState<List<UiRecordDetail>>>
        get() = _recordDetailList

    private val _nowRecord: MutableStateFlow<UiRecord>
    = MutableStateFlow(UiRecord("","",""))
    override val nowRecord: StateFlow<UiRecord>
        get() = _nowRecord

    private suspend fun _updateRecordDetailList(record: UiRecord) {
        Log.d(TAG, "$record")
        recordService.readRecordDetail(record.convertToDataRecordFromRaw())
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

    override fun updateNowRecord(record: UiRecord) {
        Log.d(TAG, _nowRecord.value.type)

        val type = record.type.split("-")[0].trim()

        var newValue = if (type == DataRecordType.Card.name ||
            type == DataRecordType.Cash.name) {
            record.copy(type = type)
        } else {
            record
        }

        newValue = newValue.copy(timestamp = newValue.timestamp.substring(0, 16))

        _nowRecord.value = newValue

        Log.d(TAG, _nowRecord.value.type)
    }

    fun request(job: suspend RecordDetailViewModel.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}