package com.dirtfy.ppp.data.logic.account

import com.dirtfy.ppp.data.source.repository.account.RepositoryAccount
import java.util.Calendar

data class ServiceAccount(
    val number: Int,
    val name: String,
    val phoneNumber: String,
    val balance: Int = 0,
    val registerTimestamp: Long = Calendar.getInstance().timeInMillis,
    val recordList: List<ServiceAccountRecord> = emptyList()
) {

    companion object {
        fun RepositoryAccount.convertToServiceAccount(): ServiceAccount {
            return ServiceAccount(
                number = number?: throw AccountException.NumberLoss(),
                name = name?: throw AccountException.NameLoss(),
                phoneNumber = phoneNumber?: throw AccountException.PhoneNumberLoss(),
                balance = balance?: throw AccountException.BalanceLoss(),
                registerTimestamp = registerTimestamp?: throw AccountException.RegisterTimestampLoss()
            )
        }
    }


    fun convertToRepositoryAccount(): RepositoryAccount {
        return RepositoryAccount(
            number = number,
            name = name,
            phoneNumber = phoneNumber,
            balance = balance,
            registerTimestamp = registerTimestamp
        )
    }
}
