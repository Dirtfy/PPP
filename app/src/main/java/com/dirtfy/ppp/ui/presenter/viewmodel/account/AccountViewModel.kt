package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiScreenState
import com.dirtfy.ppp.ui.dto.UiState
import com.dirtfy.ppp.ui.dto.account.UiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.account.UiAccountMode
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreenState
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel(), AccountController, Tagger {

    private val accountService = AccountService(AccountFireStore()) //TODO 왜 DI 안함? 뷰모델 다 안함

    private val accountList: Flow<List<UiAccount>>
            = accountService.accountStream()
        .map { it.map { account -> account.convertToUiAccount() } }

    private val searchClueFlow = MutableStateFlow("")
    private val modeFlow = MutableStateFlow(UiAccountMode.Main)
    private val nowAccountFlow = MutableStateFlow(UiAccount())

    override val uiAccountScreenState: StateFlow<UiAccountScreenState>
        = nowAccountFlow
            .combine(searchClueFlow) { nowAccount, searchClue ->
                UiAccountScreenState(
                    nowAccount = nowAccount,
                    searchClue = searchClue
                )
            }
            .combine(modeFlow) { state, mode ->
                state.copy(
                    mode = mode
                )
            }
            .combine(accountList) { state, accountList ->
                val filteredAccountList = accountList.filter {
                    it.number.contains(state.searchClue)
                }

                var newState = state.copy(
                    accountList = filteredAccountList,
                )

                if (state.accountList !== accountList)
                    newState = newState.copy(
                        accountListState = UiScreenState(state = UiState.COMPLETE)
                    )

                newState
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiAccountScreenState()
            )



    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountList() {
        // TODO 다른 기기에서 어카운트 변경시 어떻게 뷰를 변경할 지 정해야 할 듯
    }

    override suspend fun updateNowAccount(account: UiAccount) {
        nowAccountFlow.update { account }
    }

    override suspend fun updateSearchClue(clue: String) {
        searchClueFlow.update { clue }
    }

    override suspend fun setMode(mode: UiAccountMode) {
        modeFlow.update { mode }
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}