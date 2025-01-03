package com.dirtfy.ppp.ui.controller.feature.menu

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuListScreenState
import kotlinx.coroutines.flow.Flow

interface MenuListController {

    val screenData: Flow<UiMenuListScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateMenuList()
    fun retryUpdateMenuList()

    fun updateSearchClue(clue: String)
    fun setMenuListState(state: UiScreenState)
}