package com.dirtfy.ppp.ui.presenter.viewmodel.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.logic.RecordService
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordDetail.Companion.convertToUiRecordDetail
import com.dirtfy.ppp.ui.dto.record.screen.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.record.RecordDetailController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordDetailViewModel: ViewModel(), RecordDetailController, Tagger {

    private val recordService: RecordService = RecordService(RecordFireStore())

    private val _screenData = MutableStateFlow(UiRecordDetailScreenState())
    override val screenData: StateFlow<UiRecordDetailScreenState>
        get() = _screenData

    private suspend fun _updateRecordDetailList(record: UiRecord) {
        Log.d(TAG, "$record")
        Log.d(TAG, "${record.convertToDataRecordFromRaw()}")
        
        _screenData.update { it.copy(recordDetailListState = UiScreenState(UiState.LOADING)) }
        
        recordService.readRecordDetail(record.convertToDataRecordFromRaw())
            .map { it.map { data -> data.convertToUiRecordDetail() } }
            .catch { cause ->
                _screenData.update {
                    it.copy(
                        recordDetailListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .collect {
                _screenData.update { before ->
                    before.copy(
                        recordDetailList = it,
                        recordDetailListState = UiScreenState(UiState.COMPLETE)
                    )
                }
            }
    }
    override suspend fun updateRecordDetailList(record: UiRecord) {
        _updateRecordDetailList(record)
    }

    override fun updateNowRecord(record: UiRecord) {
        // TODO 의문 밖에 들지 않는 로직
        val type = record.type.split("-")[0].trim()
        var newValue = if (type == DataRecordType.Card.name ||
            type == DataRecordType.Cash.name) {
            record.copy(type = type)
        } else {
            record
        }
        newValue = newValue.copy(timestamp = newValue.timestamp.substring(0, 16))

        _screenData.update { it.copy(nowRecord = newValue) }
        request { _updateRecordDetailList(record) }
    }

    override fun request(job: suspend RecordDetailController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}