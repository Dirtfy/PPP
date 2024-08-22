package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.common.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountDetailController: Controller {

    val accountRecordList: StateFlow<FlowState<List<UiAccountRecord>>>

    val nowAccount: StateFlow<UiAccount>
    val newAccountRecord: StateFlow<UiNewAccountRecord>

    suspend fun updateAccountRecordList(accountNumber: Int)
    suspend fun updateNowAccount(account: UiAccount)
    suspend fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord)

    suspend fun addRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    )

    fun request(job: suspend AccountDetailController.() -> Unit)
}