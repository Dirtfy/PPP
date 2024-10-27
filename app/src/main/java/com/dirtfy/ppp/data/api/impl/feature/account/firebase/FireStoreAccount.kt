package com.dirtfy.ppp.data.api.impl.feature.account.firebase

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.google.firebase.Timestamp
import java.util.Date

data class FireStoreAccount(
    val number: Int?,
    val name: String?,
    val phoneNumber: String?,
    val registerTimestamp: Timestamp?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )

    companion object {
        fun DataAccount.convertToFireStoreAccount(): FireStoreAccount {
            return FireStoreAccount(
                number = number,
                name = name,
                phoneNumber = phoneNumber,
                registerTimestamp = Timestamp(Date(registerTimestamp))
            )
        }
    }

    fun convertToDataAccount(
        balance: Int
    ): DataAccount {
        return DataAccount(
            number = number?: throw AccountException.NumberLoss(),
            name = name?: throw AccountException.NameLoss(),
            phoneNumber = phoneNumber?: throw AccountException.PhoneNumberLoss(),
            balance = balance,
            registerTimestamp = registerTimestamp?.seconds?.times(1000L)
                ?: throw AccountException.RegisterTimestampLoss()
        )
    }
}
