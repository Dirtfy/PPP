package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.api.AccountApi
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordType
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordBusinessLogic @Inject constructor(
    private val accountApi: AccountApi,
    private val recordApi: RecordApi<Transaction> // TODO com.google.firebase.firestore.Transaction 숨기기
): BusinessLogic {

    fun readRecord(id: Int) = operate {
        recordApi.read(id)
    }

    fun readRecords() = operate {
        val recordList = recordApi.readAll()
            .sortedBy { -it.timestamp }
        recordList
    }

    fun readRecordDetail(record: DataRecord) = operate {
        val detailList = recordApi.readDetail(record)
        detailList
    }

    private fun isRecordTypePoint(record: DataRecord): Boolean {
        return !(record.type == DataRecordType.Card.name ||
                record.type == DataRecordType.Cash.name)
    }

    fun update(record: DataRecord) = operate {
        // TODO 조건 check

        val oldRecord = recordApi.read(record.id)
        var newRecord = record

        if (isRecordTypePoint(oldRecord) != isRecordTypePoint(newRecord)) {

            newRecord = newRecord.copy(income = -newRecord.income)
        }

        if (isRecordTypePoint(newRecord)) {

            val accountNumber = newRecord.type.toInt()
            if (!accountApi.isNumberExist(accountNumber))
                throw RecordException.InvalidAccountNumber()

            val balance = recordApi.readSumOf(
                "type", "$accountNumber",
                "income"
            )
            if (balance + newRecord.income < 0)
                throw RecordException.InvalidBalance()

        }

        recordApi.update(newRecord)
    }

    fun delete(id: Int) = operate {
        // TODO 조건 check
        recordApi.delete(id)
    }

    fun recordStream() = recordApi.recordStream()
        .map { it.sortedBy { data -> -data.timestamp } }
}