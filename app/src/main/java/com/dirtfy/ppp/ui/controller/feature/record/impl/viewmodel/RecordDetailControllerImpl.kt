package com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.RecordBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.record.RecordAtomConverter.convertToDataRecordFromRaw
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

    private lateinit var rawNowRecord: UiRecord

    private val _screenData = MutableStateFlow(UiRecordDetailScreenState())
    override val screenData: Flow<UiRecordDetailScreenState>
        get() = _screenData

    private suspend fun _updateRecordDetailList(record: UiRecord) {
        Log.d(TAG, "$record")
        Log.d(TAG, "${record.convertToDataRecordFromRaw()}")

        _screenData.update { it.copy(recordDetailListState = UiScreenState(UiState.LOADING)) }
        
        recordBusinessLogic.readRecordDetail(record.convertToDataRecordFromRaw())
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

    override suspend fun updateRecordDetailList() {
        _updateRecordDetailList(rawNowRecord)
    }

    override fun updateNowRecord(record: UiRecord) {
        rawNowRecord = record
        val newValue = record.copy(timestamp = record.timestamp.substring(0, 16))

        _screenData.update { it.copy(nowRecord = newValue) }
    }

}