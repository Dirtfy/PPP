package com.dirtfy.ppp.data.source.firestore.record

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.DataRecordDetail

data class FireStoreRecordDetail(
    val name: String?,
    val amount: Int?,
    val count: Int?
) {
    constructor() : this(null, null, null)

    companion object {
        fun DataRecordDetail.convertToFireStoreRecordDetail(): FireStoreRecordDetail {
            return FireStoreRecordDetail(
                name = name,
                amount = amount,
                count = count
            )
        }
    }

    fun convertToDataRecordDetail(): DataRecordDetail{
        return DataRecordDetail(
            name = name?: throw RecordException.DetailNameLoss(),
            amount = amount?: throw RecordException.DetailAmountLoss(),
            count = count?: throw RecordException.DetailCountLoss()
        )
    }
}
