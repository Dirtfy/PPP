package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.service.AccountService
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.dto.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val accountService: AccountService
): ViewModel(), AccountDetailController {

    private val bubbles = Bubbles()

    override val accountRecordList: StateFlow<FlowState<List<UiAccountRecord>>>
        get() = bubbles.accountRecordList.get()
    override val nowAccount: StateFlow<UiAccount>
        get() = bubbles.nowAccount.get()
    override val newAccountRecord: StateFlow<UiNewAccountRecord>
        get() = bubbles.newAccountRecord.get()

    override suspend fun updateAccountRecordList() {
        val accountNumber = nowAccount.value.number.toInt()
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

    override suspend fun addRecord(newAccountRecord: UiNewAccountRecord) {
        val accountNumber = nowAccount.value.number.toInt()
        val (issuedName, difference) = newAccountRecord
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference.toInt()
        ).conflate().collect {
            bubbles.accountRecordList.let { bubble ->
                bubble.set(it.passMap { data ->
                    val newList = bubble.value.reversed().toMutableList()
                    newList.add(data.convertToUiAccountRecord())
                    newList.reversed()
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