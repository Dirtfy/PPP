package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.dto.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class AccountDetailViewModel: ViewModel(), AccountDetailController {

    private val accountService = AccountService(AccountFireStore())

    private val bubbles = Bubbles()

    override val accountRecordList: StateFlow<FlowState<List<UiAccountRecord>>>
        get() = bubbles.accountRecordList.get()
    override val nowAccount: StateFlow<UiAccount>
        get() = bubbles.nowAccount.get()
    override val newAccountRecord: StateFlow<UiNewAccountRecord>
        get() = bubbles.newAccountRecord.get()

    override suspend fun updateAccountRecordList(accountNumber: Int) {
        accountService.readAccountRecord(accountNumber)
            .conflate().collect {
                bubbles.accountRecordList.set(
                    it.passMap { data ->
                        data.map { record ->
                            record.convertToUiAccountRecord()
                        }
                    }
                )
            }
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        bubbles.nowAccount.set(account)
    }

    override suspend fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        bubbles.newAccountRecord.set(newAccountRecord)
    }

    override suspend fun addRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) {
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference
        ).conflate().collect {
            bubbles.accountRecordList.let { bubble ->
                bubble.set(it.passMap { data ->
                    val newList = bubble.value.toMutableList()
                    newList.add(data.convertToUiAccountRecord())
                    newList
                })
            }
        }
    }

    override fun request(job: suspend AccountDetailController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}