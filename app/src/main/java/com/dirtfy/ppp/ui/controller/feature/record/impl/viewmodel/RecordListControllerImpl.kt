package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToUiRecord
import com.dirtfy.ppp.ui.controller.feature.record.RecordListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordListScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecordListControllerImpl @Inject constructor(
    private val recordBusinessLogic: RecordBusinessLogic
): RecordListController {

    private val _screenData: MutableStateFlow<UiRecordListScreenState>
            = MutableStateFlow(UiRecordListScreenState())

    private val recordListFlow = recordBusinessLogic.recordStream()
        .map {
            it.map { data -> data.convertToUiRecord() }
        }
        .catch { cause ->
            _screenData.update { it.copy(recordListState = UiScreenState(UiState.FAIL, cause.message)) }
        }

    override val screenData: Flow<UiRecordListScreenState>
        = _screenData
        .combine(recordListFlow) { state, recordList ->
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
            if (state.recordList != recordList // 내용이 달라졌을 때
                || state.recordList !== recordList // 내용이 같지만 다른 인스턴스
                || recordList == emptyList<UiRecord>()) // emptyList()는 항상 같은 인스턴스
                newState = newState.copy(
                    recordListState = UiScreenState(state = UiState.COMPLETE)
                )

            newState
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateRecordList() {
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }

}