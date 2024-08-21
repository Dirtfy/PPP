package com.dirtfy.ppp.data.source.firestore

import com.dirtfy.ppp.data.source.repository.account.RepositoryAccount
import com.google.firebase.Timestamp
import java.util.Date

data class FireStoreAccount(
    val number: Int?,
    val name: String?,
    val phoneNumber: String?,
    val balance: Int?,
    val registerTimestamp: Timestamp?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null
    )

    companion object {
        fun RepositoryAccount.convertToFireStoreAccount(): FireStoreAccount {
            return FireStoreAccount(
                number = number,
                name = name,
                phoneNumber = phoneNumber,
                balance = balance,
                registerTimestamp = Timestamp(Date(registerTimestamp!!))
            )
        }
    }
    fun convertToRepositoryAccount(): RepositoryAccount {
        return RepositoryAccount(
            number = number,
            name = name,
            phoneNumber = phoneNumber,
            balance = balance,
            registerTimestamp = registerTimestamp?.seconds?.times(1000L)
        )
    }
}
