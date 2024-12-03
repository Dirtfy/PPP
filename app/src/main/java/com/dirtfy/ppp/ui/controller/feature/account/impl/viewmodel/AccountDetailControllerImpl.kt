package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.common.converter.feature.account.AccountAtomConverter.convertToUiAccountRecord
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountDetailControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountDetailController, Tagger {

    private val retryTrigger = MutableStateFlow(0)
    private val nowAccountNumberFlow = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val accountRecordListStream: Flow<List<UiAccountRecord>>
     = retryTrigger.combine(nowAccountNumberFlow) { _, nowAccountNumber ->
        nowAccountNumber
    }.flatMapLatest { nowAccountNumber ->
        accountBusinessLogic.accountRecordStream(nowAccountNumber)
            .map {
                val recordList = it.map { account -> account.convertToUiAccountRecord() }
                _screenData.update { state -> state.copy(
                    accountRecordListState = UiScreenState(UiState.COMPLETE),
                    nowAccount = state.nowAccount.copy(
                        balance = StringFormatConverter.formatCurrency(it.sumOf { data -> data.difference })
                    )
                ) }
                recordList
            }
            .onStart {
                _screenData.update { state -> state.copy(
                    accountRecordListState = UiScreenState(UiState.LOADING)
                ) }
                emit(emptyList())
            }
            .catch { cause ->
                _screenData.update { state -> state.copy(
                    accountRecordListState = UiScreenState(UiState.FAIL, cause)
                ) }
                emit(emptyList())
            }
    }

    private val _screenData: MutableStateFlow<UiAccountDetailScreenState>
        = MutableStateFlow(UiAccountDetailScreenState())
    override val screenData: Flow<UiAccountDetailScreenState>
        = _screenData
        .combine(accountRecordListStream) { state, recordList ->
            state.copy(
                accountRecordList = recordList
            )
        }

    override fun updateNowAccount(account: UiAccount) {
        _screenData.update { it.copy(
            nowAccount = account
        ) }
        nowAccountNumberFlow.update { account.number.toInt() }
    }

    @Deprecated(
        message = "accountRecordList will be automatically updated when nowAccount is updated",
        replaceWith = ReplaceWith("updateNowAccount(account)")
    )
    override fun updateAccountRecordList(account: UiAccount) {
        updateNowAccount(account)
    }

    override fun retryUpdateAccountRecordList() {
        retryTrigger.value += 1
    }

    override fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        _screenData.update {
            it.copy(
                newAccountRecord = newAccountRecord
            )
        }
    }

    override suspend fun addRecord() {
        val accountNumber = _screenData.value.nowAccount.number.toInt()
        val (issuedName, difference) = _screenData.value.newAccountRecord
        _screenData.update { it.copy(addAccountRecordState = UiScreenState(UiState.LOADING)) }
        accountBusinessLogic.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            differenceString = difference
        ).catch { cause ->
            Log.e(TAG, "addRecord() - addAccountRecord failed \n ${cause.message}")
            _screenData.update {
                it.copy(
                    addAccountRecordState = UiScreenState(UiState.FAIL, cause)
                )
            }
        }.collect {
            _screenData.update {
                it.copy(
                    newAccountRecord = UiNewAccountRecord(),
                    addAccountRecordState = UiScreenState(UiState.COMPLETE)
                )
            }
        }
    }

    override fun setAccountRecordListState(state: UiScreenState){
        _screenData.update{
            it.copy(accountRecordListState = state)
        }
    }

    override fun setAddAccountRecordState(state: UiScreenState){
        _screenData.update{
            it.copy(addAccountRecordState = state)
        }
    }

}