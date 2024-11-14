package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.controller.feature.record.RecordDetailController
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val listController: RecordListController,
    private val detailController: RecordDetailController
): ViewModel(), RecordController, Tagger {

    private val modeFlow = MutableStateFlow(UiRecordMode.Main)
    private val nowRecordStateFlow = MutableStateFlow(UiScreenState(UiState.COMPLETE))

    override val screenData: StateFlow<UiRecordScreenState>
        = modeFlow
            .combine(nowRecordStateFlow) { mode, nowRecordState ->
                UiRecordScreenState(
                    mode = mode,
                    nowRecordState = nowRecordState
                )
            }.combine(listController.screenData) { state, listScreenData ->
                state.copy(
                    recordList = listScreenData.recordList,
                    searchClue = listScreenData.searchClue,
                    recordListState = listScreenData.recordListState
                )
            }.combine(detailController.screenData) { state, detailScreenData ->
                state.copy(
                    recordDetailList = detailScreenData.recordDetailList,
                    nowRecord = detailScreenData.nowRecord,
                    recordDetailListState = detailScreenData.recordDetailListState
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiRecordScreenState()
            )

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateRecordList() {
    }

    override suspend fun updateRecordDetailList() {
        detailController.updateRecordDetailList()
    }

    override fun updateSearchClue(clue: String) {
        listController.updateSearchClue(clue)
    }

    override fun updateNowRecord(record: UiRecord) {
        val rawValue = listController.findRawRecord(record)
        if(rawValue == null)
            nowRecordStateFlow.update {
                // TODO 이거 에러 처리 하는 부분 뷰 레이어에 필요함.
                UiScreenState(UiState.FAIL, RecordException.NonExistQuery().message)
            }
        else {
            detailController.updateNowRecord(rawValue)
            nowRecordStateFlow.update { UiScreenState(UiState.COMPLETE) }
        }
    }

    override fun setMode(mode: UiRecordMode) {
        modeFlow.update { mode }
    }

    override fun request(job: suspend RecordController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}