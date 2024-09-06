package com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.account

import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.test.data.logic.AccountLogic
import com.dirtfy.ppp.test.ui.dto.account.UiAccount
import com.dirtfy.ppp.test.ui.dto.account.UiAccountDetailState
import com.dirtfy.ppp.test.ui.presenter.contract.account.AccountDetailController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val account: UiAccount,
    private val accountLogic: AccountLogic
): ViewModel(), AccountDetailController {

    override val uiState: StateFlow<UiAccountDetailState>
        get() = TODO("Not yet implemented")

    override fun updateUserName(name: String) {
        TODO("Not yet implemented")
    }

    override fun updateDifference(difference: String) {
        TODO("Not yet implemented")
    }

    override fun addRecord() {
        TODO("Not yet implemented")
    }




}