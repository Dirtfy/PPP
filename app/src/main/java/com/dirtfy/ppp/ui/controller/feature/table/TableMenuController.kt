package com.dirtfy.ppp.ui.controller.feature.table

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMenuScreenState
import kotlinx.coroutines.flow.Flow

interface TableMenuController {
    val screenData: Flow<UiTableMenuScreenState>

    suspend fun updateMenuList()
    fun setMenuListState(state: UiScreenState)
}