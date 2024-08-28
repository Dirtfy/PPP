package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.UiAccountMode
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel(), AccountController {

    private val accountService = AccountService(AccountFireStore())

    private val bubbles = Bubbles()

    override val accountList: StateFlow<FlowState<List<UiAccount>>>
        get() = bubbles.accountList.get()

    override val searchClue: StateFlow<String>
        get() = bubbles.searchClue.get()
    override val nowAccount: StateFlow<UiAccount>
        get() = bubbles.nowAccount.get()
    override val mode: StateFlow<UiAccountMode>
        get() = bubbles.mode.get()


    override suspend fun updateAccountList() {
        accountService.readAllAccounts().conflate().collect {
            bubbles.accountList.let { bubble ->
                bubble.set(it.passMap { data ->
                    data.map { account -> account.convertToUiAccount() }
                })
            }
        }
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        bubbles.nowAccount.set(account)
    }

    override suspend fun updateSearchClue(clue: String) {
        bubbles.searchClue.set(clue)
        accountService.readAllAccounts()
            .conflate().collect {
                bubbles.accountList.let { bubble ->
                    bubble.set(it.passMap { data ->
                        val filter = data.map { account -> account.number.toString() }
                            .filter { number -> number.contains(clue) }

                        data.map { account -> account.convertToUiAccount() }
                            .filter { account -> filter.contains(account.number) }
                    })
                }
            }
    }

    override suspend fun setMode(mode: UiAccountMode) {
        bubbles.mode.set(mode)
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}