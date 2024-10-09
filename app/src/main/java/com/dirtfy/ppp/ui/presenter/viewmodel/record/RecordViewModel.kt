package com.dirtfy.ppp.ui.presenter.viewmodel.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.logic.RecordService
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.ui.dto.record.UiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecord.Companion.convertToRawUiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecord.Companion.convertToUiRecord
import com.dirtfy.ppp.ui.dto.record.UiRecordMode
import com.dirtfy.ppp.ui.presenter.controller.record.RecordController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class RecordViewModel: ViewModel(), RecordController, Tagger {

    private val recordService: RecordService = RecordService(RecordFireStore())

    private var _rawRecordList: List<UiRecord>
    = emptyList()
    private val _recordList: MutableStateFlow<FlowState<List<UiRecord>>>
    = MutableStateFlow(FlowState.loading())
    override val recordList: StateFlow<FlowState<List<UiRecord>>>
        get() = _recordList

    private val _searchClue: MutableStateFlow<String>
    = MutableStateFlow("")
    override val searchClue: StateFlow<String>
        get() = _searchClue

    private val _nowRecord: MutableStateFlow<UiRecord>
    = MutableStateFlow(UiRecord("", "", ""))
    override val nowRecord: StateFlow<UiRecord>
        get() = _nowRecord

    private val _mode: MutableStateFlow<UiRecordMode>
    = MutableStateFlow(UiRecordMode.Main)
    override val mode: StateFlow<UiRecordMode>
        get() = _mode

    private suspend fun _updateRecordList() {
        recordService.readRecords().conflate().collect {
            _recordList.value = it.passMap { data ->

                _rawRecordList = data.map { record -> record.convertToRawUiRecord() }

                data.map { record -> record.convertToUiRecord() }
            }
        }
    }
    override fun updateRecordList() = request {
        _updateRecordList()
    }

    override fun updateSearchClue(clue: String) = request {
        _searchClue.value = clue
    }

    override fun updateNowRecord(record: UiRecord) = request {
        val rawValue = _rawRecordList.find {
            Log.d(TAG,"${it.timestamp.substring(0, 16)} - ${record.timestamp}")
            it.timestamp.substring(0, 16) == record.timestamp
        }
            ?: throw RecordException.NonExistQuery()
        _nowRecord.value = rawValue // TODO issued name 어떻게 주지?
    }

    override fun setMode(mode: UiRecordMode) = request {
        _mode.value = mode
    }

    fun request(job: suspend RecordViewModel.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}