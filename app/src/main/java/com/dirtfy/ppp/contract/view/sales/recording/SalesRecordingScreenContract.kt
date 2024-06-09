package com.dirtfy.ppp.contract.view.sales.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.SalesRecordingContract

object SalesRecordingScreenContract {

    interface API {

        @Composable
        fun RecordList(
            recordList: List<SalesRecordingContract.DTO.Record>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: SalesRecordingContract.DTO.Record,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun RecordDetail(
            record: SalesRecordingContract.DTO.Record,
            menuList: List<SalesRecordingContract.DTO.Menu>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: SalesRecordingContract.API,
            user: User,
            modifier: Modifier
        )

    }
}