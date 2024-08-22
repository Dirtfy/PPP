package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.dto.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
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
    override val isAccountCreateMode: StateFlow<Boolean>
        get() = bubbles.isAccountCreateMode.get()
    override val isAccountUpdateMode: StateFlow<Boolean>
        get() = bubbles.isAccountUpdateMode.get()
    override val isAccountDetailMode: StateFlow<Boolean>
        get() = bubbles.isAccountDetailMode.get()

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

    override suspend fun setAccountCreateMode(mode: Boolean) {
        bubbles.isAccountCreateMode.set(mode)
    }

    override suspend fun setAccountUpdateMode(mode: Boolean) {
        bubbles.isAccountUpdateMode.set(mode)
    }

    override suspend fun setAccountDetailMode(mode: Boolean) {
        bubbles.isAccountDetailMode.set(mode)
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}