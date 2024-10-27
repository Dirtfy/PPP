package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class AccountBusinessLogic @Inject constructor(
    private val accountApi: AccountApi
): BusinessLogic {

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("""^010-\d{3,4}-\d{4}$""")
        return regex.matches(phoneNumber)
    }

    fun createAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = operate {
        val account = accountApi.let {
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
        val maxAccountNumber = accountApi.getMaxAccountNumber()
        var candidate = Random.nextInt(maxAccountNumber)
        while(accountApi.isSameNumberExist(candidate)) {
            candidate = Random.nextInt(maxAccountNumber)
        }

        candidate
    }

    fun readAllAccounts() = operate {
        accountApi.readAllAccount()
    }

    fun updateAccount(
        number: Int,
        name: String,
        phoneNumber: String
    ) = operate {
        val account = accountApi.let {
            if (!it.isNumberExist(number))
                throw AccountException.InvalidNumber()

            if (isValidPhoneNumber(phoneNumber))
                throw AccountException.InvalidPhoneNumber()

            it.update(
                accountApi.find(number).copy(
                    name = name,
                    phoneNumber = phoneNumber
                )
            )
        }

        account
    }

    // TODO deprecate 시키기
    fun readAccountRecord(accountNumber: Int) = operate {
        if (!accountApi.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        accountApi.readAllRecord(accountNumber).sortedBy { -it.timestamp }
    }

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) = operate {
        if (!accountApi.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        val currentBalance = accountApi.readAccountBalance(accountNumber)

        val result = currentBalance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        val record = accountApi.createRecord(
            accountNumber = accountNumber,
            record = DataAccountRecord(
                issuedName = issuedName,
                difference = difference,
                result = result
            )
        )

        record
    }

    fun accountStream() = accountApi.accountStream()

    fun accountRecordStream(accountNumber: Int) =
        accountApi.accountRecordStream(accountNumber)
            .map { it.sortedBy { data -> -data.timestamp } }
}