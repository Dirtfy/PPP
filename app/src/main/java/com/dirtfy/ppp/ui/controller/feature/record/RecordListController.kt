package com.dirtfy.ppp.ui.controller.feature.record

import com.dirtfy.ppp.ui.state.feature.record.UiRecordListScreenState
import kotlinx.coroutines.flow.Flow

interface RecordListController {
    val screenData: Flow<UiRecordListScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateRecordList()
    fun updateSearchClue(clue: String)
}