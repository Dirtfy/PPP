package com.dirtfy.ppp.ui.dto.account

import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.ui.dto.account.UiAccount.Companion.convertToUiAccount

data class UiNewAccount(
    val number: String = "",
    val name: String = "",
    val phoneNumber: String = ""
) {

    companion object {
        fun UiAccount.convertToUiNewAccount(): UiNewAccount {
            return UiNewAccount(
                number = number,
                name = name,
                phoneNumber = phoneNumber
            )
        }
    }
    fun convertToUiAccount(): UiAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).convertToUiAccount()
    }
}
