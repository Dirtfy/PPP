package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountController: Controller {

    val uiAccountScreen: StateFlow<UiAccountScreen>

    suspend fun updateAccountList()
    suspend fun updateNowAccount(account: UiAccount)
    suspend fun updateSearchClue(clue: String)

    suspend fun setMode(mode: UiAccountMode)


    fun request(job: suspend AccountController.() -> Unit)

}