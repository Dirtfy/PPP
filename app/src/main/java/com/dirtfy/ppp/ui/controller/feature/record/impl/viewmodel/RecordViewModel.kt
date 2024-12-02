package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.controller.feature.record.RecordDetailController
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
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

    override val screenData: StateFlow<UiRecordScreenState>
        = modeFlow
            .combine(listController.screenData) { mode, listScreenData ->
                UiRecordScreenState(
                    mode = mode,
                    recordList = listScreenData.recordList,
                    searchClue = listScreenData.searchClue,
                    recordListState = listScreenData.recordListState
                )
            }.combine(detailController.screenData) { state, detailScreenData ->
                state.copy(
                    nowRecord = detailScreenData.nowRecord,
                    recordDetailList = detailScreenData.recordDetailList,
                    nowRecordState = detailScreenData.nowRecordState,
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

    override fun retryUpdateRecordList() {
        listController.retryUpdateRecordList()
    }

    override suspend fun updateRecordDetailList() {
        detailController.updateRecordDetailList()
    }

    override fun updateSearchClue(clue: String) {
        listController.updateSearchClue(clue)
    }

    override suspend fun updateNowRecord(record: UiRecord) {
        detailController.updateNowRecord(record)
    }

    override fun setMode(mode: UiRecordMode) {
        modeFlow.update { mode }
    }

    override fun setRecordListState(state: UiScreenState) {
        listController.setRecordListState(state)
    }

    override fun setRecordDetailListState(state: UiScreenState) {
        detailController.setRecordDetailListState(state)
    }

    override fun setNowRecordState(state: UiScreenState) {
        detailController.setNowRecordState(state)
    }

    override fun request(job: suspend RecordController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}