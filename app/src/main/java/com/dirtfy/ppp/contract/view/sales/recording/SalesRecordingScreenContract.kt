package com.dirtfy.ppp.contract.view.sales.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object SalesRecordingScreenContract {

    object DTO {

        data class Record(
            val totalPrice: String,
            val payment: String
        )

        data class Menu(
            val name: String,
            val price: String,
            val count: String
        )

    }

    interface API {

        @Composable
        fun RecordList(
            recordList: List<DTO.Record>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: DTO.Record,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordDetail(
            record: DTO.Record,
            menuList: List<DTO.Menu>,
            user: User,
            modifier: Modifier
        )

    }
}