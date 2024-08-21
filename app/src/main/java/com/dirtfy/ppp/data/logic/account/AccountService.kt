package com.dirtfy.ppp.data.logic.account

import com.dirtfy.ppp.data.logic.Service
import com.dirtfy.ppp.data.source.repository.account.AccountRepository
import com.dirtfy.ppp.data.source.repository.account.RepositoryAccount
import com.dirtfy.ppp.data.source.repository.account.record.AccountRecordRepository
import com.dirtfy.ppp.data.source.repository.account.record.RepositoryAccountRecord

class AccountService(
    val accountRepository: AccountRepository,
    val accountRecordRepository: AccountRecordRepository
): Service {

    private fun RepositoryAccount.convertToServiceAccount(): ServiceAccount {
        return ServiceAccount(
            number = number?: throw AccountException.NumberLoss(),
            name = name?: throw AccountException.NameLoss(),
            phoneNumber = phoneNumber?: throw AccountException.PhoneNumberLoss(),
            balance = balance?: throw AccountException.BalanceLoss(),
            registerTimestamp = registerTimestamp?: throw AccountException.RegisterTimestampLoss()
        )
    }

    private fun ServiceAccount.convertToRepositoryAccount(): RepositoryAccount {
        return RepositoryAccount(
            number = number,
            name = name,
            phoneNumber = phoneNumber,
            balance = balance,
            registerTimestamp = registerTimestamp
        )
    }

    private fun RepositoryAccountRecord.convertToServiceAccountRecord(): ServiceAccountRecord {
        return ServiceAccountRecord(
            issuedName = issuedName?: throw AccountException.RecordIssuedNameLoss(),
            difference = difference?: throw AccountException.RecordAmountLoss(),
            result = result?: throw AccountException.RecordResultLoss(),
            timestamp = timestamp?: throw AccountException.RecordTimestampLoss()
        )
    }
    private fun ServiceAccountRecord.convertToRepositoryAccountRecord(): RepositoryAccountRecord {
        return RepositoryAccountRecord(
            issuedName = issuedName,
            difference = difference,
            result = result,
            timestamp = timestamp
        )
    }

    fun createAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = asFlow {
        accountRepository.let {
            if (it.isSameNumberExist(number))
                throw AccountException.NonUniqueNumber()

            val regex = Regex("""^010-\d{3,4}-\d{4}$""")
            if (!regex.matches(phoneNumber))
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

    fun readAccount() = asFlow {
        accountRepository.readAll().map {
            it.convertToServiceAccount()
        }
    }

    fun updateAccount(account: ServiceAccount) = asFlow {
        accountRepository.let {
            if (!it.isNumberExist(account.number))
                throw AccountException.InvalidNumber()

            if (account.balance < 0)
                throw AccountException.InvalidBalance()

            it.update(
                account.convertToRepositoryAccount()
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