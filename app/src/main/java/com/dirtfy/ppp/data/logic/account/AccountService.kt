package com.dirtfy.ppp.data.logic.account

import com.dirtfy.ppp.data.logic.Service
import com.dirtfy.ppp.data.logic.account.ServiceAccount.Companion.convertToServiceAccount
import com.dirtfy.ppp.data.logic.account.ServiceAccountRecord.Companion.convertToServiceAccountRecord
import com.dirtfy.ppp.data.source.repository.account.AccountRepository
import com.dirtfy.ppp.data.source.repository.account.record.AccountRecordRepository
import kotlin.random.Random

class AccountService(
    val accountRepository: AccountRepository,
    val accountRecordRepository: AccountRecordRepository
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
                ServiceAccount(
                    number = number,
                    name = name,
                    phoneNumber = phoneNumber
                ).convertToRepositoryAccount()
            ).convertToServiceAccount()
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
        accountRepository.readAll().map {
            it.convertToServiceAccount()
        }
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
            ).convertToServiceAccount()
        }
    }

    fun readAccountRecord(accountNumber: Int) = asFlow {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val recordList = accountRecordRepository.readAll().map {
            it.convertToServiceAccountRecord()
        }

        accountRepository.find(accountNumber)
            .convertToServiceAccount()
            .copy(recordList = recordList)
    }

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) = asFlow {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val currentAccount = accountRepository.find(accountNumber)
            .convertToServiceAccount()

        val result = currentAccount.balance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        val createdRecord = accountRecordRepository.create(
            ServiceAccountRecord(
                accountNumber = accountNumber,
                issuedName = issuedName,
                difference = difference,
                result = result
            ).convertToRepositoryAccountRecord()
        ).convertToServiceAccountRecord()

        val currentRecordList = currentAccount.recordList.toMutableList()

        currentAccount.copy(
            recordList = currentRecordList + createdRecord
        )
    }
}