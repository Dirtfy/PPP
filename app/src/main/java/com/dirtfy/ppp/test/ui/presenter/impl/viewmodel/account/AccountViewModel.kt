package com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.test.data.logic.AccountLogic
import com.dirtfy.ppp.test.ui.dto.account.UiAccount
import com.dirtfy.ppp.test.ui.dto.account.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.test.ui.dto.account.UiAccountState
import com.dirtfy.ppp.test.ui.presenter.contract.account.AccountController
import com.dirtfy.ppp.ui.dto.UiAccountMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountLogic: AccountLogic
): ViewModel(), AccountController {

    private val accountList: StateFlow<List<UiAccount>>
            = accountLogic.accountStream()
        .map { it.map { account -> account.convertToUiAccount() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val searchClue: MutableStateFlow<String>
            = MutableStateFlow("")

    private val mode: MutableStateFlow<UiAccountMode>
            = MutableStateFlow(UiAccountMode.Main)

    override val uiState: StateFlow<UiAccountState>
        get() = accountList
            .combine(searchClue) { accountList, searchClue ->
                UiAccountState(
                    accountList = accountList,
                    searchClue = searchClue
                )
            }
            .combine(mode) { state, mode ->
                state.copy(mode = mode, isFailed = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiAccountState()
            )

    private var _selectedAccount: UiAccount
    = UiAccount()
    override val selectedAccount: UiAccount
        get() = _selectedAccount

    override fun updateSearchClue(clue: String)
            = searchClue.update { clue }

    override fun setMode(targetMode: UiAccountMode)
            = mode.update { targetMode }

    override fun onAccountClick(account: UiAccount) {
        _selectedAccount = account
        mode.update { UiAccountMode.Detail }
    }

    private fun request(job: suspend () -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}