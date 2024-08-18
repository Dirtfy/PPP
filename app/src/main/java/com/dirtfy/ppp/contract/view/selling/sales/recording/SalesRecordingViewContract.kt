package com.dirtfy.ppp.contract.view.selling.sales.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract

object SalesRecordingViewContract {

    interface API {

        @Composable
        fun RecordList(
            recordList: List<SalesRecordingViewModelContract.DTO.Record>,
            viewModel: SalesRecordingViewModelContract.RecordList.API,
            modifier: Modifier
        )

        @Composable
        fun RecordItem(
            record: SalesRecordingViewModelContract.DTO.Record,
            viewModel: SalesRecordingViewModelContract.RecordList.API,
            modifier: Modifier
        )

        @Composable
        fun RecordDetail(
            record: SalesRecordingViewModelContract.DTO.Record,
            menuList: List<SalesRecordingViewModelContract.DTO.Menu>,
            viewModel: SalesRecordingViewModelContract.RecordDetail.API,
            modifier: Modifier
        )

        @Composable
        fun Main(
            viewModel: SalesRecordingViewModelContract.API,
            modifier: Modifier
        )

    }
}