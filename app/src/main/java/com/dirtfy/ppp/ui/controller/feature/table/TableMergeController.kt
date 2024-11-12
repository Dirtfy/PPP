package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMergeScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import kotlinx.coroutines.flow.Flow

interface TableMergeController {
    val screenData: Flow<UiTableMergeScreenState>

    suspend fun updateTableList()
    fun clickTableOnMergeMode(table: UiTable)
    fun clickTableOnMainOrOrderMode(table: UiTable): Int
    suspend fun mergeTable()
    fun cancelMergeTable()
    fun disbandGroup(tableNumber: Int)
    fun setMode(mode: UiScreenState)
}