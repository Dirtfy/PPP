package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.source.repository.AccountRepository
import kotlinx.coroutines.flow.map
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
    ) = operate {
        val account = accountRepository.let {
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

        account
    }

    fun createAccountNumber() = operate {
        val maxAccountNumber = accountRepository.getMaxAccountNumber()
        var candidate = Random.nextInt(maxAccountNumber)
        while(accountRepository.isSameNumberExist(candidate)) {
            candidate = Random.nextInt(maxAccountNumber)
        }

        candidate
    }

    fun readAllAccounts() = operate {
        accountRepository.readAllAccount()
    }

    fun updateAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = operate {
        val account = accountRepository.let {
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

        account
    }

    // TODO deprecate 시키기
    fun readAccountRecord(accountNumber: Int) = operate {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        accountRepository.readAllRecord(accountNumber).sortedBy { -it.timestamp }
    }

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) = operate {
        if (!accountRepository.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val currentBalance = accountRepository.readAccountBalance(accountNumber)

        val result = currentBalance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        val record = accountRepository.createRecord(
            accountNumber = accountNumber,
            record = DataAccountRecord(
                issuedName = issuedName,
                difference = difference,
                result = result
            )
        )

        record
    }

    fun accountStream() = accountRepository.accountStream()

    fun accountRecordStream(accountNumber: Int) =
        accountRepository.accountRecordStream(accountNumber)
            .map { it.sortedBy { data -> -data.timestamp } }
}