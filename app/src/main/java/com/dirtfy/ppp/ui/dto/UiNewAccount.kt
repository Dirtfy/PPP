package com.dirtfy.ppp.ui.dto

import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.ui.dto.UiAccount.Companion.convertToUiAccount

data class UiNewAccount(
    val number: String = "",
    val name: String = "",
    val phoneNumber: String = ""
) {
    fun convertToUiAccount(): UiAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).convertToUiAccount()
    }
}
