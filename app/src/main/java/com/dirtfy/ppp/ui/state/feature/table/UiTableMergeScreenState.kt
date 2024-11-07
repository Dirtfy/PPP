package com.dirtfy.ppp.ui.state.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable

data class UiTableMergeScreenState(
    val tableList: List<UiTable> = emptyList(),
    val sourceTableList: List<UiTable> = emptyList(),

    val tableListState: UiScreenState = UiScreenState(),
    val mergeTableState: UiScreenState = UiScreenState(UiState.COMPLETE),
)
