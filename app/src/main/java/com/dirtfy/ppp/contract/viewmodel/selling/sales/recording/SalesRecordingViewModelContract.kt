package com.dirtfy.ppp.contract.viewmodel.selling.sales.recording

import androidx.compose.runtime.State

object SalesRecordingViewModelContract {

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

    object RecordList {
        interface API {
            val recordList: State<List<DTO.Record>>
            val selectedRecord: State<DTO.Record?>

            fun checkSalesRecordList()

            fun clickRecord(record: DTO.Record)
        }
    }

    object RecordDetail {
        interface API {
            val selectedRecord: State<DTO.Record?>
            val recordDetail: State<List<DTO.Menu>>

            fun checkRecordDetail()
        }
    }

    interface API: RecordList.API, RecordDetail.API
}