package com.dirtfy.ppp.contract.viewmodel

import com.dirtfy.ppp.contract.user.selling.sales.recording.SalesRecordingUser
import kotlinx.coroutines.flow.StateFlow

object SalesRecordingContract {

    object DTO {
        data class Record(
            val timestamp: String,
            val totalPrice: String,
            val payment: String
        )

        data class Menu(
            val name: String,
            val price: String,
            val count: String
        )
    }

    interface API: SalesRecordingUser {
        val recordList: StateFlow<List<DTO.Record>>
        val selectedRecord: StateFlow<DTO.Record?>
        val selectedRecordMenuList: StateFlow<List<DTO.Menu>>
    }
}