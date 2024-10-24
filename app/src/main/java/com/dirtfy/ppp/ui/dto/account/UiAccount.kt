package com.dirtfy.ppp.ui.dto.account

import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiAccount(
    val number: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val balance: String = "",
    val registerTimestamp: String = ""
) {

    companion object {
        fun DataAccount.convertToUiAccount(): UiAccount {
            return UiAccount(
                number = number.toString(),
                name = name,
                phoneNumber = phoneNumber,
                balance = Utils.formatCurrency(balance),
                registerTimestamp = Utils.formatTimestampFromDay(registerTimestamp)
            )
        }
    }

    fun convertToDataAccount(): DataAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber,
            registerTimestamp = Utils.parseTimestampFromDay(registerTimestamp)
        )
    }
}
