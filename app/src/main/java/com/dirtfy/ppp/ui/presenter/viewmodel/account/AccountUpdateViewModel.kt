package com.dirtfy.ppp.ui.presenter.viewmodel.account

import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.data.logic.AccountService
import com.dirtfy.ppp.data.source.firestore.account.AccountFireStore
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.controller.account.AccountUpdateController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate

class AccountUpdateViewModel: ViewModel(), AccountUpdateController {

    private val accountService = AccountService(AccountFireStore())

    private val bubbles = Bubbles()

    override val nowAccount: StateFlow<UiAccount>
        get() = bubbles.nowAccount.get()

    override suspend fun updateAccount(newAccountData: UiNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.updateAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
            bubbles.accountList.let { bubble ->
                bubble.set(it.passMap { data ->
                    val newList = bubble.value.toMutableList()

                    newList.replaceAll { account ->
                        if (account.number.toInt() == data.number)
                            account.copy(
                                number = number,
                                name = name,
                                phoneNumber = phoneNumber
                            )
                        else
                            account
                    }

                    newList
                })
            }
        }
    }

}