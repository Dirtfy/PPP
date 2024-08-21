package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.presenter.controller.Controller
import kotlinx.coroutines.flow.StateFlow

interface AccountController: Controller {

    val accountList: StateFlow<FlowState<List<ControllerAccount>>>

    val searchClue: StateFlow<String>
    val newAccount: StateFlow<ControllerNewAccount>

    val isAccountCreateMode: StateFlow<Boolean>

    suspend fun updateAccountList()
    suspend fun updateNewAccount(
        newAccountData: ControllerNewAccount
    )
    suspend fun updateSearchClue(clue: String)

    suspend fun addAccount(
        newAccountData: ControllerNewAccount
    )
    suspend fun setRandomValidAccountNumberToNewAccount()
    suspend fun setAccountCreateMode(mode: Boolean)
    suspend fun updateAccount(
        newAccountData: ControllerNewAccount
    )

    suspend fun addRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    )

    fun request(job: suspend AccountController.() -> Unit)

}