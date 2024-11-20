package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToDataRecordFromNowRecord
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToNowRecord
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToUiRecordDetail
import com.dirtfy.ppp.ui.controller.feature.record.RecordDetailController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.UiRecordDetailScreenState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecordDetailControllerImpl @Inject constructor(
    private val recordBusinessLogic: RecordBusinessLogic
): RecordDetailController, Tagger {

    private val _screenData = MutableStateFlow(UiRecordDetailScreenState())
    override val screenData: Flow<UiRecordDetailScreenState>
        get() = _screenData

    private suspend fun _updateRecordDetailList(record: UiRecord) {
        _screenData.update { it.copy(recordDetailListState = UiScreenState(UiState.LOADING)) }
        
        recordBusinessLogic.readRecordDetail(record.convertToDataRecordFromNowRecord())
            .map { it.map { data -> data.convertToUiRecordDetail() } }
            .catch { cause ->
                _screenData.update {
                    it.copy(
                        recordDetailListState = UiScreenState(UiState.FAIL, cause)
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

    override suspend fun updateRecordDetailList() {
        _updateRecordDetailList(_screenData.value.nowRecord)
    }

    override suspend fun updateNowRecord(record: UiRecord) {
        _screenData.update { it.copy(nowRecordState = UiScreenState(UiState.LOADING)) }
        recordBusinessLogic.readRecord(record.id.toInt())
            .catch { cause ->
                _screenData.update { it.copy(
                    nowRecordState = UiScreenState(UiState.FAIL, cause.message)
                ) }
            }
            .collect { data ->
                _screenData.update { it.copy(
                    nowRecord = data.convertToNowRecord(),
                    nowRecordState = UiScreenState(UiState.COMPLETE)
                ) }
            }
    }

}