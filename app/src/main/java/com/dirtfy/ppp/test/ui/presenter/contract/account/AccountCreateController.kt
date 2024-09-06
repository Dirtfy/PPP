package com.dirtfy.ppp.test.ui.presenter.contract.account

import com.dirtfy.ppp.test.ui.dto.account.UiAccountCreateState
import com.dirtfy.ppp.test.ui.presenter.contract.Controller

interface AccountCreateController: Controller<UiAccountCreateState> {

    fun updateAccountNumber(number: String)
    fun updateAccountName(name: String)
    fun updatePhoneNumber(phoneNumber: String)

    fun addAccount()
    fun setRandomValidAccountNumberToNewAccount()

}