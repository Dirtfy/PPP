package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.api.impl.feature.account.firebase.AccountFireStore
import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountDetailViewModel: ViewModel(), AccountDetailController, Tagger {

    private val accountService = AccountBusinessLogic(AccountFireStore())

    private val _screenData: MutableStateFlow<UiAccountDetailScreenState>
        = MutableStateFlow(
        UiAccountDetailScreenState(
            nowAccount = UiAccount(number = "0")
        )
    )
    override val screenData: StateFlow<UiAccountDetailScreenState>
        get() = _screenData

    private lateinit var accountRecordListStream: Flow<List<UiAccountRecord>>

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountRecordList() {

    }

    override suspend fun updateNowAccount(account: UiAccount) {
        _screenData.update {
            it.copy(
                nowAccount = account,
                accountRecordListState = UiScreenState(UiState.LOADING)
            )
        }
        accountRecordListStream = accountService.accountRecordStream(account.number.toInt())
            .map { it.map { account -> account.convertToUiAccountRecord() } }

        // TODO 더 나은 해결책 강구
        accountRecordListStream
            .catch { cause ->
                Log.e(TAG, "updateNowAccount() - stream failed \n ${cause.message}")
                _screenData.update {
                    it.copy(
                        accountRecordListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .conflate().collect {
                _screenData.update { before ->
                    before.copy(
                        accountRecordList = it,
                        accountRecordListState = UiScreenState(UiState.COMPLETE)
                    )
                }
            }
    }

    override fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        _screenData.update {
            it.copy(
                newAccountRecord = newAccountRecord
            )
        }
    }

    override suspend fun addRecord(newAccountRecord: UiNewAccountRecord) {
        val accountNumber = _screenData.value.nowAccount.number.toInt()
        val (issuedName, difference) = newAccountRecord
        _screenData.update { it.copy(newAccountRecordState = UiScreenState(UiState.LOADING)) }
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference.toInt()
        ).catch { cause ->
            Log.e(TAG, "addRecord() - addAccountRecord failed \n ${cause.message}")
            _screenData.update {
                it.copy(
                    newAccountRecordState = UiScreenState(UiState.FAIL, cause.message)
                )
            }
        }.collect {
            _screenData.update {
                it.copy(
                    newAccountRecord = UiNewAccountRecord(),
                    newAccountRecordState = UiScreenState(UiState.COMPLETE)
                )
            }
        }
    }

    override fun request(job: suspend AccountDetailController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}