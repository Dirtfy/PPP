package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountController: Controller {

    val accountList: StateFlow<FlowState<List<UiAccount>>>

    val searchClue: StateFlow<String>

    val nowAccount: StateFlow<UiAccount>

    val isAccountCreateMode: StateFlow<Boolean>
    val isAccountUpdateMode: StateFlow<Boolean>
    val isAccountDetailMode: StateFlow<Boolean>

    suspend fun updateAccountList()
    suspend fun updateNowAccount(account: UiAccount)
    suspend fun updateSearchClue(clue: String)

    suspend fun setAccountCreateMode(mode: Boolean)
    suspend fun setAccountUpdateMode(mode: Boolean)
    suspend fun setAccountDetailMode(mode: Boolean)


    fun request(job: suspend AccountController.() -> Unit)

}