package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountDetailController: Controller {

//    val accountRecordList: StateFlow<FlowState<List<UiAccountRecord>>>
//
//    val nowAccount: StateFlow<UiAccount>
//    val newAccountRecord: StateFlow<UiNewAccountRecord>
    val uiAccountScreen: StateFlow<UiAccountScreen>

    suspend fun updateAccountRecordList()
    suspend fun updateNowAccount(account: UiAccount)
    suspend fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)

    suspend fun addRecord(newAccountRecord: UiNewAccountRecord)

    fun request(job: suspend AccountDetailController.() -> Unit)
}