package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToUiRecord
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordListScreenState
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

    override val screenData: Flow<UiRecordListScreenState>
        = _screenData
        .combine(recordListFlow) { state, recordList ->
            // TODO searchClue 구현되면 여기서 filtering
            state.copy(
                recordList = recordList
            )
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateRecordList() {
    }

    override fun retryUpdateRecordList() {
        retryTrigger.value += 1
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }

    override fun setRecordListState(state: UiScreenState) {
        _screenData.update { it.copy(recordListState = state) }
    }

}