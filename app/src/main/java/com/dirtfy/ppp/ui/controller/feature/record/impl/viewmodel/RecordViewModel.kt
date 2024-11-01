package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToRawUiRecord
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToUiRecord
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    recordBusinessLogic: RecordBusinessLogic
): ViewModel(), RecordController, Tagger {

    private val searchClueFlow = MutableStateFlow("")
    private val nowRecordFlow = MutableStateFlow(UiRecord())
    private val modeFlow = MutableStateFlow(UiRecordMode.Main)
    private val rawRecordListFlow = MutableStateFlow(emptyList<UiRecord>())
    private val nowRecordStateFlow = MutableStateFlow(UiScreenState(UiState.COMPLETE))

    private val recordStream = recordBusinessLogic.recordStream()
        .map {
            rawRecordListFlow.value = it.map { data -> data.convertToRawUiRecord() }
            it.map { data -> data.convertToUiRecord() }
        }

    override val screenData: StateFlow<UiRecordScreenState>
        = searchClueFlow
            .combine(modeFlow) { searchClue, mode ->
                UiRecordScreenState(
                    searchClue = searchClue,
                    mode = mode
                )
            }.combine(nowRecordFlow) { state, nowRecord ->
                state.copy(
                    nowRecord = nowRecord
                )
            }.combine(nowRecordStateFlow) { state, nowRecordState ->
                state.copy(
                    nowRecordState = nowRecordState
                )
            }.combine(recordStream) { state, recordList ->
                // TODO searchClue 구현되면 여기서 filtering
                /*val filteredAccountList = recordList.filter {
                    it.number.contains(state.searchClue)
                }
                var newState = state.copy(
                    accountList = filteredAccountList,
                )*/
                var newState = state.copy(
                    recordList = recordList
                )
                if (state.recordList != recordList /* 내용이 달라졌을 때 */
                    || state.recordList !== recordList /* 내용이 같지만 다른 인스턴스 */
                    || recordList == emptyList<UiRecord>() /* emptyList()는 항상 같은 인스턴스 */)
                    newState = newState.copy(
                        recordListState = UiScreenState(state = UiState.COMPLETE)
                    )

                newState
            }.catch { cause ->
                // TODO 더 기가 막힌 방법 생각해보기
                UiRecordScreenState(
                    searchClue = searchClueFlow.value,
                    mode = modeFlow.value,
                    nowRecord = nowRecordFlow.value,
                    nowRecordState = nowRecordStateFlow.value,
                    recordListState = UiScreenState(UiState.FAIL, cause.message)
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiRecordScreenState()
            )

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateRecordList() {
    }

    override fun updateSearchClue(clue: String) {
        searchClueFlow.update { clue }
    }

    override fun updateNowRecord(record: UiRecord) {
        val rawValue = rawRecordListFlow.value.find {
            Log.d(TAG,"${it.timestamp} - ${record.timestamp}")
            it.timestamp == record.timestamp
        }
        if(rawValue == null)
            nowRecordStateFlow.update {
                // TODO 이거 에러 처리 하는 부분 뷰 레이어에 필요함.
                UiScreenState(UiState.FAIL, RecordException.NonExistQuery().message)
            }
        else {
            nowRecordFlow.update { rawValue } // TODO issued name 어떻게 주지?
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