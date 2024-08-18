package com.dirtfy.ppp.contract.model.selling

import com.dirtfy.ppp.common.Repository
import com.google.firebase.Timestamp
import java.util.Date

object SalesRecordModelContract {
    object DTO {
        data class Sales(
            val salesID: String?,
            val timestamp: Timestamp = Timestamp(Date()),
            val menuCountMap: Map<String, Int>,
            val menuPriceMap: Map<String, Int>,
            val pointAccountNumber: String?
        )
    }

    interface API: Repository<DTO.Sales>
}