package com.dirtfy.ppp.test.data.logic.impl

import android.media.audiofx.AcousticEchoCanceler
import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.DataAccount
import com.dirtfy.ppp.data.dto.DataAccountRecord
import com.dirtfy.ppp.data.source.repository.AccountRepository
import com.dirtfy.ppp.test.data.logic.AccountLogic
import com.dirtfy.ppp.test.data.source.AccountSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject
import kotlin.random.Random

class AccountService @Inject constructor(
    private val accountSource: AccountSource
): AccountLogic {

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("""^010-\d{3,4}-\d{4}$""")
        return regex.matches(phoneNumber)
    }

    override fun createAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = flow<DataAccount> {
        accountSource.let {
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

    override fun createAccountNumber() = flow<Int> {
        val maxAccountNumber = accountSource.getMaxAccountNumber()
        var candidate = Random.nextInt(maxAccountNumber)
        while(accountSource.isSameNumberExist(candidate)) {
            candidate = Random.nextInt(maxAccountNumber)
        }
        candidate
    }

    override fun readAllAccounts() = flow<List<DataAccount>> {
        accountSource.readAllAccount()
    }

    override fun updateAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = flow<DataAccount> {
        accountSource.let {
            if (!it.isNumberExist(number))
                throw AccountException.InvalidNumber()

            if (isValidPhoneNumber(phoneNumber))
                throw AccountException.InvalidPhoneNumber()

            it.update(
                accountSource.find(number).copy(
                    name = name,
                    phoneNumber = phoneNumber
                )
            )
        }
    }

    override fun readAccountRecord(accountNumber: Int) = flow<List<DataAccountRecord>> {
        if (!accountSource.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        accountSource.readAllRecord(accountNumber).sortedBy { -it.timestamp }
    }

    override fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) = flow<DataAccountRecord> {
        if (!accountSource.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val currentBalance = accountSource.readAccountBalance(accountNumber)

        val result = currentBalance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        accountSource.createRecord(
            accountNumber = accountNumber,
            record = DataAccountRecord(
                issuedName = issuedName,
                difference = difference,
                result = result
            )
        )
    }

    override fun accountStream() = accountSource.accountStream()

    override fun accountRecordStream(accountNumber: Int)
    = accountSource.accountRecordStream(accountNumber)
}