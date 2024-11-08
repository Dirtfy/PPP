package com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel

import com.dirtfy.ppp.data.logic.AccountBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.account.AccountAtomConverter.convertToUiAccount
import com.dirtfy.ppp.ui.controller.feature.account.AccountListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.UiAccountListScreenState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountListControllerImpl @Inject constructor(
    private val accountBusinessLogic: AccountBusinessLogic
): AccountListController {

    private val accountListFlow = accountBusinessLogic.accountStream()
        .catch { cause ->
            _screenData.update { it.copy(accountListState = UiScreenState(UiState.FAIL, cause.message)) }
        }
        .map { it.map { account -> account.convertToUiAccount() } }

    private val _screenData: MutableStateFlow<UiAccountListScreenState>
        = MutableStateFlow(UiAccountListScreenState())
    override val screenData: Flow<UiAccountListScreenState>
        = _screenData
        .combine(accountListFlow) { state, accountList ->
            val filteredAccountList = accountList.filter {
                it.number.contains(state.searchClue)
            }

            var newState = state.copy(
                accountList = filteredAccountList,
            )

            if (state.accountList != accountList // 내용이 달라졌을 때
                || state.accountList !== accountList // 내용이 같지만 다른 인스턴스
                || accountList == emptyList<UiAccount>()) // emptyList()는 항상 같은 인스턴스
                newState = newState.copy(
                    accountListState = UiScreenState(state = UiState.COMPLETE)
                )

            newState
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateAccountList() {
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }

}