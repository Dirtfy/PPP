package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.UiAccountScreen
import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountDetailViewModel: ViewModel(), AccountDetailController {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountScreen = MutableStateFlow(UiAccountScreen())
    override val uiAccountScreen: StateFlow<UiAccountScreen>
        get() = _uiAccountScreen

    override suspend fun updateAccountRecordList() {
        val accountNumber = _uiAccountScreen.value.nowAccount.number.toInt()
        accountService.readAccountRecord(accountNumber).collect { flowState ->
            _uiAccountScreen.update {
                it.copy(accountRecordList = flowState.passMap { data ->
                    data.map { record -> record.convertToUiAccountRecord() }
                })
            }
        }
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        _uiAccountScreen.update { it.copy(nowAccount = account) }
    }

    override suspend fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        _uiAccountScreen.update { it.copy(newAccountRecord = newAccountRecord) }
    }

    override suspend fun addRecord(newAccountRecord: UiNewAccountRecord) {
        val accountNumber = _uiAccountScreen.value.nowAccount.number.toInt()
        val (issuedName, difference) = newAccountRecord
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference.toInt()
        ).conflate().collect {
            _uiAccountScreen.update { before ->
                before.copy(accountRecordList = before.accountRecordList.passMap { native ->
                    val newList = native.reversed().toMutableList()
                    it.passMap { data ->
                        newList.add(data.convertToUiAccountRecord())
                    }
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