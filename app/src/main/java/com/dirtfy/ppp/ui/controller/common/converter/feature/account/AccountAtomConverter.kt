package com.dirtfy.ppp.ui.controller.common.converter.feature.account

import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiAccountRecord
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.state.feature.account.atom.UiUpdateAccount

object AccountAtomConverter {

    fun DataAccount.convertToUiAccount(
        balance: Int
    ): UiAccount {
        return UiAccount(
            number = number.toString(),
            name = name,
            phoneNumber = phoneNumber,
            balance = StringFormatConverter.formatCurrency(balance),
            registerTimestamp = StringFormatConverter.formatTimestampFromDay(registerTimestamp)
        )
    }

    fun DataAccountRecord.convertToUiAccountRecord(): UiAccountRecord {
        return UiAccountRecord(
            issuedName = issuedName,
            difference = StringFormatConverter.formatCurrency(difference),
            result = StringFormatConverter.formatCurrency(result),
            timestamp = StringFormatConverter.formatTimestampFromMinute(timestamp)
        )
    }

    fun UiAccount.convertToUiNewAccount(): UiNewAccount {
        return UiNewAccount(
            number = number,
            name = name,
            phoneNumber = phoneNumber
        )
    }

    fun UiAccount.convertToUiUpdateAccount(): UiUpdateAccount {
        return UiUpdateAccount(
            number = number,
            name = name,
            phoneNumber = phoneNumber
        )
    }



    fun UiAccount.convertToDataAccount(): DataAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber,
            registerTimestamp = StringFormatConverter.parseTimestampFromDay(registerTimestamp)
        )
    }

    fun UiAccountRecord.convertToDataAccountRecord(): DataAccountRecord {
        return DataAccountRecord(
            issuedName = issuedName,
            difference = StringFormatConverter.parseCurrency(difference),
            result = StringFormatConverter.parseCurrency(result),
            timestamp = StringFormatConverter.parseTimestampFromMinute(timestamp)
        )
    }

    fun UiNewAccount.convertToUiAccount(
        balance: Int
    ): UiAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).convertToUiAccount(balance)
    }

    fun UiUpdateAccount.convertToUiAccount(
        balance: Int
    ): UiAccount {
        return DataAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).convertToUiAccount(balance)
    }
}