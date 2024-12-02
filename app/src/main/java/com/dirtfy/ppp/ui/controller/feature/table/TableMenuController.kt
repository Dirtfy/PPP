package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMenuScreenState
import kotlinx.coroutines.flow.Flow

interface TableMenuController {
    val screenData: Flow<UiTableMenuScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()
    fun retryUpdateMenuList()
    fun setMenuListState(state: UiScreenState)
}