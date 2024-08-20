package com.dirtfy.ppp.contract.model.selling

import com.dirtfy.ppp.common.Repository
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

object SalesRecordModelContract {
    object DTO {
        data class Sales(
            val salesID: String?,
            val timestamp: Long = Calendar.getInstance().timeInMillis,
            val menuCountMap: Map<String, Int>,
            val menuPriceMap: Map<String, Int>,
            val pointAccountNumber: String?
        )
    }

    interface API: Repository<DTO.Sales>
}