package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.account.UiAccount.Companion.convertToUiAccount
import com.dirtfy.ppp.ui.dto.account.screen.UiAccountScreen
import com.dirtfy.ppp.ui.dto.account.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountUpdateController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.update

class AccountUpdateViewModel: ViewModel(), AccountUpdateController {

    private val accountService = AccountService(AccountFireStore())

    private val _uiAccountScreen = MutableStateFlow(UiAccountScreen())
    override val uiAccountScreen: StateFlow<UiAccountScreen>
        get() = _uiAccountScreen

    override suspend fun updateAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.updateAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
            _uiAccountScreen.update { before ->
                before.copy(accountList = before.accountList.passMap { uiAccountList ->
                    val newList = uiAccountList.toMutableList()
//                    it.passMap { data ->
//                        newList.replaceAll { uiAccount ->
//                            if (uiAccount.number.toInt() == data.number) data.convertToUiAccount()
//                            else uiAccount
//                        }
//                    }
                    newList
                })
            }
        }
    }

}