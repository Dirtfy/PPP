package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.AccountException
import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.dto.feature.account.DataAccount
import com.dirtfy.ppp.data.dto.feature.account.DataAccountRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import com.dirtfy.ppp.data.logic.common.converter.RecordDataConverter.convertToDataAccountRecord
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class AccountBusinessLogic @Inject constructor(
    private val accountApi: AccountApi,
    private val recordApi: RecordApi<Transaction>
): BusinessLogic {

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("""^010-\d{3,4}-\d{4}$""") // TODO 053 등 집전화 적용?
        return regex.matches(phoneNumber)
    }

    fun createAccount(
        numberString: String,
        name: String,
        phoneNumber: String
    ) = operate {
        if(numberString == "")throw AccountException.BlankNumber()
        else if(name == "")throw AccountException.BlankName()
        else if(phoneNumber == "")throw AccountException.BlankPhoneNumber()

        val number = numberString.toInt()
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
        numberString: String,
        name: String,
        phoneNumber: String
    ) = operate {
        if(numberString == "") throw AccountException.BlankNumber()
        else if(name == "") throw AccountException.BlankName()
        else if(phoneNumber == "") throw AccountException.BlankPhoneNumber()

        val number = numberString.toInt()
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

    fun readAccountRecord(accountNumber: Int) = operate {
        if (!accountApi.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()

        recordApi.readRecordWith("type", "$accountNumber").sortedBy { -it.timestamp }
    }

    private suspend fun readBalance(
        accountNumber: Int
    ): Int {
        return recordApi.readSumOf(
            "type", "$accountNumber",
            "income"
        )
    }

    private suspend fun createAccountRecord(
        accountNumber: Int,
        accountRecord: DataAccountRecord
    ): DataAccountRecord {
        val record = DataRecord(
            income = accountRecord.difference,
            type = accountNumber.toString(),
            issuedBy = accountRecord.issuedName
        )

        recordApi.create(record, emptyList())

        return accountRecord
    }

    fun addAccountRecord(
        accountNumber: Int,
        issuedName: String,
        differenceString: String
    ) = operate {
        if (!accountApi.isNumberExist(accountNumber))
            throw AccountException.InvalidNumber()
        if(issuedName == "") throw AccountException.BlankIssuedName()
        if(differenceString == "") throw AccountException.BlankDifference()

        val difference = differenceString.toInt()
        val currentBalance = readBalance(accountNumber)

        val result = currentBalance + difference
        if (result < 0)
            throw AccountException.InvalidBalance()

        val record = createAccountRecord(
            accountNumber = accountNumber,
            accountRecord = DataAccountRecord(
                issuedName = issuedName,
                difference = difference,
                result = result
            )
        )

        record
    }

    fun accountStream() = accountApi.accountStream()

    fun accountRecordStream(accountNumber: Int) =
        recordApi.recordStreamWith("type", "$accountNumber")
            .map { it.sortedBy { data -> -data.timestamp } }
            .map {
                var result = 0
                val accountRecordList = mutableListOf<DataAccountRecord>()
                for (record in it) {
                    result += record.income

                    accountRecordList.add(
                        record.convertToDataAccountRecord(result)
                    )
                }

                accountRecordList
            }

    fun accountBalanceStream(accountNumber: Int) =
        recordApi.recordStreamSumOf("type", "$accountNumber", "income") {
            it.sumOf { record -> record.income }
        }
}