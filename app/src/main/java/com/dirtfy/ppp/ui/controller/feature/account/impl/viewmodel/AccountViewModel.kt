package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.ui.controller.feature.account.AccountController
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.controller.feature.account.AccountListController
import com.dirtfy.ppp.ui.controller.feature.account.AccountUpdateController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountMode
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccountRecord
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountListController: AccountListController,
    private val accountCreateController: AccountCreateController,
    private val accountDetailController: AccountDetailController,
    private val accountUpdateController: AccountUpdateController
): ViewModel(), AccountController, Tagger {

    private val _screenData: MutableStateFlow<UiAccountScreenState>
        = MutableStateFlow(UiAccountScreenState())
    override val screenData: StateFlow<UiAccountScreenState>
        = _screenData
        .combine(accountListController.screenData) { state, listScreenData ->
            state.copy(
                accountList = listScreenData.accountList,
                searchClue = listScreenData.searchClue,
                accountListState = listScreenData.accountListState
            )
        }.combine(accountCreateController.screenData) { state, createScreenData ->
            state.copy(
                newAccount = createScreenData.newAccount,
                addAccountState = createScreenData.addAccountState,
                numberGeneratingState = createScreenData.numberGeneratingState
            )
        }.combine(accountDetailController.screenData) { state, detailScreenData ->
            state.copy(
                nowAccount = detailScreenData.nowAccount,
                newAccountRecord = detailScreenData.newAccountRecord,
                accountRecordList = detailScreenData.accountRecordList,
                accountRecordListState = detailScreenData.accountRecordListState,
                addAccountRecordState = detailScreenData.addAccountRecordState
            )
        }.combine(accountUpdateController.screenData) { state, updateScreenData ->
            state.copy(
                updateAccount = updateScreenData.updateAccount,
                updateAccountState = updateScreenData.updateAccountState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiAccountScreenState()
        )


    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountList() {}

    override fun retryUpdateAccountList() {
        accountListController.retryUpdateAccountList()
    }

    override fun updateNewAccount(newAccountData: UiNewAccount) {
        accountCreateController.updateNewAccount(newAccountData)
    }

    override suspend fun addAccount() {
        accountCreateController.addAccount { setMode(UiAccountMode.Main) }
    }

    override suspend fun setRandomValidAccountNumberToNewAccount() {
        accountCreateController.setRandomValidAccountNumberToNewAccount()
    }

    @Deprecated(
        message = "accountRecordList will be automatically updated when nowAccount is updated",
        replaceWith = ReplaceWith("updateNowAccount(account)")
    )

    override fun retryUpdateAccountRecordList() {
        accountDetailController.retryUpdateAccountRecordList()
    }

    override fun updateNowAccount(account: UiAccount) {
        accountDetailController.updateNowAccount(account)
    }

    override fun updateNewAccountRecord(newAccountRecord: UiNewAccountRecord) {
        accountDetailController.updateNewAccountRecord(newAccountRecord)
    }

    override suspend fun addRecord() {
        accountDetailController.addRecord()
    }

    override suspend fun updateAccount(newAccountData: UiNewAccount) {
        accountUpdateController.updateAccount(newAccountData)
    }

    override fun updateSearchClue(clue: String) {
        accountListController.updateSearchClue(clue)
    }

    override fun setMode(mode: UiAccountMode) {
        _screenData.update { it.copy(mode = mode) }
    }

    override fun setAddAccountState(state: UiScreenState){
        accountCreateController.setAddAccountState(state)
    }

    override fun setNumberGeneratingState(state: UiScreenState) {
        accountCreateController.setNumberGeneratingState(state)
    }

    override fun setAccountRecordListState(state: UiScreenState){
        accountDetailController.setAccountRecordListState(state)
    }

    override fun setAddAccountRecordState(state: UiScreenState){
        accountDetailController.setAddAccountRecordState(state)
    }

    override fun setAccountListState(state: UiScreenState) {
        accountListController.setAccountListState(state)
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}