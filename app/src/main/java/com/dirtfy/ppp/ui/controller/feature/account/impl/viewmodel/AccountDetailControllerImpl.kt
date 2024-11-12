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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountDetailControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountDetailController, Tagger {

    private val _screenData: MutableStateFlow<UiAccountDetailScreenState>
        = MutableStateFlow(
        UiAccountDetailScreenState(
            nowAccount = UiAccount(number = "0")
        )
    )
    override val screenData: Flow<UiAccountDetailScreenState>
        get() = _screenData // 안될거같음

    private lateinit var accountRecordListStream: Flow<List<UiAccountRecord>>

    override suspend fun updateAccountRecordList(account: UiAccount) {
        _screenData.update {
            it.copy(
                nowAccount = account,
                accountRecordListState = UiScreenState(UiState.LOADING)
            )
        }
        println("accountRecordListStream init")
        accountRecordListStream = accountBusinessLogic.accountRecordStream(account.number.toInt())
            .map { it.map { account -> account.convertToUiAccountRecord() } }

        println("accountRecordListStream collect")
        // TODO 더 나은 해결책 강구
        accountRecordListStream
            .catch { cause ->
                Log.e(TAG, "updateAccountRecordList() - stream failed \n ${cause.message}")
                _screenData.update {
                    it.copy(
                        accountRecordListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .conflate().collect { listUiRecord ->
                println("accountRecordListStream collected")
                _screenData.update { before ->
                    before.copy(
                        nowAccount = account.copy(
                            balance = listUiRecord.sumOf { uiRecord -> StringFormatConverter.parseCurrency(uiRecord.difference) }.toString()
                        ),
                        accountRecordList = listUiRecord,
                        accountRecordListState = UiScreenState(UiState.COMPLETE)
                    )
                }
                println("screenData updated")
                listUiRecord.forEach { println(it.toString()) }
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
        accountBusinessLogic.addAccountRecord(
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

}