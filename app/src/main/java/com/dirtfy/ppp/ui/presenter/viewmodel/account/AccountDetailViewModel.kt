package com.dirtfy.ppp.ui.presenter.viewmodel.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiAccountRecord.Companion.convertToUiAccountRecord
import com.dirtfy.ppp.ui.dto.account.UiNewAccountRecord
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountDetailScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountDetailController
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

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountDetailScreenState: MutableStateFlow<UiAccountDetailScreenState>
        = MutableStateFlow(UiAccountDetailScreenState(nowAccount = UiAccount(number = "0")))
    override val uiAccountDetailScreenState: StateFlow<UiAccountDetailScreenState>
        get() = _uiAccountDetailScreenState

    private lateinit var accountRecordListStream: Flow<List<UiAccountRecord>>

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountRecordList() {

    }

    override suspend fun updateNowAccount(account: UiAccount) {
        _uiAccountDetailScreenState.update {
            it.copy(
                nowAccount = account,
                accountRecordListState = UiScreenState()
            )
        }
        accountRecordListStream = accountService.accountRecordStream(account.number.toInt())
            .map { it.map { account -> account.convertToUiAccountRecord() } }

        // TODO 더 나은 해결책 강구
        accountRecordListStream
            .catch { cause ->
                Log.e(TAG, "updateNowAccount() - stream failed \n ${cause.message}")
                _uiAccountDetailScreenState.update {
                    it.copy(
                        accountRecordListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .conflate().collect {
                _uiAccountDetailScreenState.update { before ->
                    before.copy(
                        accountRecordList = it,
                        accountRecordListState = UiScreenState(UiState.COMPLETE)
                    )
                }
            }
    }

    override fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        _uiAccountDetailScreenState.update {
            it.copy(
                newAccountRecord = newAccountRecord
            )
        }
    }

    override suspend fun addRecord(newAccountRecord: UiNewAccountRecord) {
        val accountNumber = _uiAccountDetailScreenState.value.nowAccount.number.toInt()
        val (issuedName, difference) = newAccountRecord
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference.toInt()
        ).catch { cause ->
            Log.e(TAG, "addRecord() - addAccountRecord failed \n ${cause.message}")
            _uiAccountDetailScreenState.update {
                it.copy(
                    newAccountRecordState = UiScreenState(UiState.FAIL, cause.message)
                )
            }
        }.collect {
            _uiAccountDetailScreenState.update {
                it.copy(
                    newAccountRecord = UiNewAccountRecord()
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