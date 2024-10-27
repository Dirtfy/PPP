package com.dirtfy.ppp.ui.state.feature.account.atom

import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount.Companion.convertToUiAccount

data class UiUpdateAccount(
    val number: String = "",
    val name: String = "",
    val phoneNumber: String = ""
)
{
    companion object {
        fun UiAccount.convertToUiUpdateAccount(): UiUpdateAccount {
            return UiUpdateAccount(
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