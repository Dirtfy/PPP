package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountDetailViewModel: ViewModel(), AccountDetailController {

    private val accountService = AccountService(AccountFireStore())

//    private val _uiAccountDetailScreenState: MutableStateFlow<UiAccountDetailScreenState>
//        = MutableStateFlow(UiAccountDetailScreenState())
    override val uiAccountDetailScreenState: StateFlow<UiAccountDetailScreenState>
        get() = nowAccountFlow
            .combine(newAccountRecordFlow) { nowAccount, newAccountRecord ->
                UiAccountDetailScreenState(
                    nowAccount = nowAccount,
                    newAccountRecord = newAccountRecord
                )
            }
            .combine(accountRecordListFlow) { state, accountRecordList ->
                var newState = state.copy(
                    accountRecordList = accountRecordList
                )
                // TODO 왜 또 얘는 로딩만 주구장창 뜨는데
                if (state.accountRecordList !== accountRecordList)
                    newState = newState.copy(
                        accountRecordListState = UiScreenState(state = UiState.COMPLETE)
                    )
                newState
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiAccountDetailScreenState()
            )

    private val nowAccountFlow: MutableStateFlow<UiAccount>
            = MutableStateFlow(UiAccount())
    private val newAccountRecordFlow: MutableStateFlow<UiNewAccountRecord>
            = MutableStateFlow(UiNewAccountRecord())
    private val accountRecordListFlow: StateFlow<List<UiAccountRecord>>
        = flow {
            accountService.accountRecordStream(nowAccountFlow.value.number.toInt()).collect {
                emit(it)
            }
        }.map { it.map { account -> account.convertToUiAccountRecord() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
//        = accountService.accountRecordStream(if (nowAccountFlow.value.number.isEmpty()) 0 else nowAccountFlow.value.number.toInt())
//        .map { it.map { account -> account.convertToUiAccountRecord() } }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

    override suspend fun updateAccountRecordList() {
//        val accountNumber = _uiAccountDetailScreenState.value.nowAccount.number.toInt()
//        accountService.readAccountRecord(accountNumber).collect { flowState ->
//            _uiAccountScreen.update {
//                it.copy(accountRecordList = flowState.passMap { data ->
//                    data.map { record -> record.convertToUiAccountRecord() }
//                })
//            }
//        }
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        nowAccountFlow.update { account }
    }

    override suspend fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        newAccountRecordFlow.update { newAccountRecord }
    }

    override suspend fun addRecord(newAccountRecord: UiNewAccountRecord) {
        val accountNumber = nowAccountFlow.value.number.toInt()
        val (issuedName, difference) = newAccountRecord
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference.toInt()
        ).conflate().collect {
//            _uiAccountDetailScreenState.update { before ->
//                before.copy(accountRecordList = before.accountRecordList.passMap { native ->
//                    val newList = native.reversed().toMutableList()
//                    it.passMap { data ->
//                        newList.add(data.convertToUiAccountRecord())
//                    }
//                    newList.reversed()
//                })
//            }
        }
    }

    override fun request(job: suspend AccountDetailController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}