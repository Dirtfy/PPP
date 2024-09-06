package com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.account

import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.test.data.logic.AccountLogic
import com.dirtfy.ppp.test.ui.dto.account.UiAccountCreateState
import com.dirtfy.ppp.test.ui.presenter.contract.account.AccountCreateController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountCreateViewModel @Inject constructor(
    private val accountLogic: AccountLogic
): ViewModel(), AccountCreateController {

    override val uiState: StateFlow<UiAccountCreateState>
        get() = TODO("Not yet implemented")

    override fun updateAccountNumber(number: String) {
        TODO("Not yet implemented")
    }

    override fun updateAccountName(name: String) {
        TODO("Not yet implemented")
    }

    override fun updatePhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun addAccount() {
        TODO("Not yet implemented")
    }

    override fun setRandomValidAccountNumberToNewAccount() {
        TODO("Not yet implemented")
    }
}