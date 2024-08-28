package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountMode
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountController: Controller {

    val accountList: StateFlow<FlowState<List<UiAccount>>>

    val searchClue: StateFlow<String>

    val nowAccount: StateFlow<UiAccount>

    val mode: StateFlow<UiAccountMode>

    suspend fun updateAccountList()
    suspend fun updateNowAccount(account: UiAccount)
    suspend fun updateSearchClue(clue: String)

    suspend fun setMode(mode: UiAccountMode)


    fun request(job: suspend AccountController.() -> Unit)

}