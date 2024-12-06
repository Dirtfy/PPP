package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableListScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import kotlinx.coroutines.flow.Flow

interface TableListController {
    val screenData: Flow<UiTableListScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateTableList()
    fun getGroupNumber(tableNumber: Int): Int
    fun getGroup(groupNumber: Int): DataTableGroup
    fun clickTableOnMergeMode(table: UiTable)
    fun clickTableOnMainOrOrderMode(table: UiTable)
    suspend fun trySetMergeMode()
    suspend fun escapeFromMergeMode()
    suspend fun mergeTable()
    fun cancelMergeTable()
    suspend fun dissolveGroup(groupNumber: Int)
    fun syncTableList()
    fun retryUpdateTableList()
    fun setMode(mode: UiTableMode)
    fun setTableListState(state: UiScreenState)
    fun setTrySetMergeModeState(state: UiScreenState)
    fun setEscapeFromMergeModeState(state: UiScreenState)
    fun setMergeTableState(state: UiScreenState)
}