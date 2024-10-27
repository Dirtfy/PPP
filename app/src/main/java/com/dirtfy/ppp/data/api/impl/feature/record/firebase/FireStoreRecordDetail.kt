package com.dirtfy.ppp.data.api.impl.feature.record.firebase

import com.dirtfy.ppp.common.exception.RecordException
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail

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

    fun convertToDataRecordDetail(): DataRecordDetail {
        return DataRecordDetail(
            name = name?: throw RecordException.DetailNameLoss(),
            amount = amount?: throw RecordException.DetailAmountLoss(),
            count = count?: throw RecordException.DetailCountLoss()
        )
    }
}
