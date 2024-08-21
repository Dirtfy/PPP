package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.data.logic.account.ServiceAccount
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccount.Companion.convertToControllerAccount

data class ControllerNewAccount(
    val number: String = "",
    val name: String = "",
    val phoneNumber: String = ""
) {
    fun convertToControllerAccount(): ControllerAccount {
        return ServiceAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).convertToControllerAccount()
    }
}
