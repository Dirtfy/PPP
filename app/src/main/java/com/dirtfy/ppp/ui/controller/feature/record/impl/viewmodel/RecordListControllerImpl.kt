package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToUiRecord
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordListScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecordListControllerImpl @Inject constructor(
    private val recordBusinessLogic: RecordBusinessLogic
): RecordListController {

    private val retryTrigger = MutableStateFlow(0)

    private val _screenData: MutableStateFlow<UiRecordListScreenState>
            = MutableStateFlow(UiRecordListScreenState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val recordListFlow = retryTrigger
        .flatMapLatest {
            recordBusinessLogic.recordStream()
                .map {
                    setRecordListState(UiScreenState(UiState.COMPLETE))

                    val recordList = it.map { data -> data.convertToUiRecord() }

                    recordList
                }
                .onStart {
                    setRecordListState(UiScreenState(UiState.LOADING))
                    emit(emptyList())
                }
                .catch { cause ->
                    setRecordListState(UiScreenState(UiState.FAIL, cause))
                    emit(emptyList())
                }
        }

    private val dateRangeFlow = MutableStateFlow(Pair(-1L, -1L))

    override val screenData: Flow<UiRecordListScreenState>
        = _screenData
        .combine(recordListFlow) { state, recordList ->
            state.copy(
                recordList = recordList
            )
        }
        .combine(dateRangeFlow) { state, dateRange ->
            // TODO searchClue 구현되면 여기서 filtering
            val start = dateRange.first
            val end = dateRange.second

            val filtered = state.recordList.filter { dataRecord ->
                if (start == -1L || end == -1L) {
                    true
                } else {
                    val timestamp = StringFormatConverter
                        .parseTimestampFromSecond(dataRecord.timestamp)

                    timestamp in start..end
                }
            }

            state.copy(
                recordList = filtered
            )
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateRecordList() {
    }

    override fun retryUpdateRecordList() {
        retryTrigger.value += 1
    }

    override fun updateDateRange(start: Long, end: Long) {
        _screenData.update { it.copy(dateRange = Pair(start, end)) }
        dateRangeFlow.update { Pair(start, end) }
    }

    override fun setRecordListState(state: UiScreenState) {
        _screenData.update { it.copy(recordListState = state) }
    }

}