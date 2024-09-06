package com.dirtfy.ppp.data.logic.service

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.source.repository.AccountRepository
import javax.inject.Inject
import kotlin.random.Random

class AccountService @Inject constructor(
    private val accountRepository: AccountRepository
): Service {

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("""^010-\d{3,4}-\d{4}$""")
        return regex.matches(phoneNumber)
    }

    fun createAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = asFlow {
        accountRepository.let {
            if (it.isSameNumberExist(number))
                throw AccountException.NonUniqueNumber()

            if (number !in 0 until it.getMaxAccountNumber())
                throw AccountException.InvalidNumber()

            if (!isValidPhoneNumber(phoneNumber))
                throw AccountException.InvalidPhoneNumber()

            it.create(
                DataAccount(
                    number = number,
                    name = name,
                    phoneNumber = phoneNumber
                )
            )
        }
    }

    fun createAccountNumber() = asFlow {
        val maxAccountNumber = accountRepository.getMaxAccountNumber()
        var candidate = Random.nextInt(maxAccountNumber)
        while(accountRepository.isSameNumberExist(candidate)) {
            candidate = Random.nextInt(maxAccountNumber)
        }
        candidate
    }

    fun readAllAccounts() = asFlow {
        accountRepository.readAllAccount()
    }

    fun updateAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = asFlow {
        accountRepository.let {
            if (!it.isNumberExist(number))
                throw AccountException.InvalidNumber()

            if (isValidPhoneNumber(phoneNumber))
                throw AccountException.InvalidPhoneNumber()

            it.update(
                accountRepository.find(number).copy(
                    name = name,
                    phoneNumber = phoneNumber
                )
            )
        }
    }

    fun readAccountRecord(accountNumber: Int) = asFlow {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        accountRepository.readAllRecord(accountNumber).sortedBy { -it.timestamp }
    }

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) = asFlow {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val currentBalance = accountRepository.readAccountBalance(accountNumber)

        val result = currentBalance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        accountRepository.createRecord(
            accountNumber = accountNumber,
            record = DataAccountRecord(
                issuedName = issuedName,
                difference = difference,
                result = result
            )
        )
    }
}