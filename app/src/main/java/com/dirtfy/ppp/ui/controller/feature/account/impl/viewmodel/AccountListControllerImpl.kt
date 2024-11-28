package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.account.AccountAtomConverter.convertToUiAccount
import com.dirtfy.ppp.ui.controller.feature.account.AccountListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountListScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountListControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountListController {

    private val retryTrigger = MutableStateFlow(0)
    private val accountListFlow = retryTrigger
        .flatMapLatest {
            accountBusinessLogic.accountStream()
                .map {
                    setAccountListState(UiScreenState(UiState.COMPLETE))
                    it.map { account -> account.convertToUiAccount(0) }
                }
                .onStart {
                    setAccountListState(UiScreenState(UiState.LOADING))
                    emit(emptyList())
                }
                .catch { cause ->
                    setAccountListState(UiScreenState(UiState.FAIL, cause))
                    emit(emptyList())
                }
        }

    private val _screenData: MutableStateFlow<UiAccountListScreenState>
        = MutableStateFlow(UiAccountListScreenState())
    override val screenData: Flow<UiAccountListScreenState>
        = _screenData
        .combine(accountListFlow) { state, accountList ->
            val filteredAccountList = accountList.filter {
                it.number.contains(state.searchClue)
            }

            state.copy(
                accountList = filteredAccountList,
            )
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountList() {
    }

    override fun retryUpdateAccountList() {
        retryTrigger.value += 1
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }

    override fun setAccountListState(state: UiScreenState){
        _screenData.update{
            it.copy(accountListState = state)
        }
    }
}