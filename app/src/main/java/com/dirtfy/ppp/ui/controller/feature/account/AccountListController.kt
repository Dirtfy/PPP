package com.dirtfy.ppp.ui.controller.feature.account

import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountListScreenState
import kotlinx.coroutines.flow.Flow

interface AccountListController {
    val screenData: Flow<UiAccountListScreenState>

    @Deprecated("screen state synchronized with repository")
    suspend fun updateAccountList()
    fun updateSearchClue(clue: String)
    fun setAccountListState(state: UiScreenState)
}