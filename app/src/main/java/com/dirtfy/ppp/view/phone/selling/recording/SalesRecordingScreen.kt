package com.dirtfy.ppp.view.phone.selling.recording

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dirtfy.ppp.common.DummySalesRecordingViewModel
import com.dirtfy.ppp.contract.view.selling.sales.recording.SalesRecordingViewContract
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme
import com.dirtfy.tagger.Tagger

object SalesRecordingScreen : SalesRecordingViewContract.API, Tagger {
    @Composable
    override fun RecordList(
        recordList: List<SalesRecordingViewModelContract.DTO.Record>,
        viewModel: SalesRecordingViewModelContract.API,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
        ) {
            items(recordList) {
                RecordItem(
                    record = it,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun RecordItem(
        record: SalesRecordingViewModelContract.DTO.Record,
        viewModel: SalesRecordingViewModelContract.API,
        modifier: Modifier
    ) {
        ListItem(
            overlineContent = { Text(text = record.timestamp) },
            headlineContent = { Text(text = record.totalPrice) },
            supportingContent = { Text(text = record.payment) },
            modifier = modifier.combinedClickable(
                onClick = {
                    viewModel.clickRecord(record)
                    viewModel.checkRecordDetail()
                    Log.d(TAG, "item clicked")
                }
            )
        )
    }

    @Composable
    override fun RecordDetail(
        record: SalesRecordingViewModelContract.DTO.Record,
        menuList: List<SalesRecordingViewModelContract.DTO.Menu>,
        viewModel: SalesRecordingViewModelContract.API,
        modifier: Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column {
                        Text(text = record.timestamp)
                        Text(text = record.payment)
                    }

                    Spacer(Modifier.size(20.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = record.totalPrice, fontSize = 35.sp)
                    }
                }

            }

            Spacer(Modifier.size(10.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(menuList) {
                        Row{
                            Text(text = it.name)
                            Spacer(Modifier.size(10.dp))
                            Text(text = it.price)
                            Spacer(Modifier.size(10.dp))
                            Text(text = it.count)
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun Main(
        viewModel: SalesRecordingViewModelContract.API,
        modifier: Modifier
    ) {
        val recordList by viewModel.recordList

        val selectedRecordCollect by viewModel.selectedRecord

        val menuList by viewModel.recordDetail

        LaunchedEffect(key1 = viewModel) {
            viewModel.checkSalesRecordList()
        }

//        ConstraintLayout(
//            modifier = modifier
//        ) {
//            val (detail, list) = createRefs()
//
//
//        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            if (selectedRecordCollect != null) {
                RecordDetail(
                    record = selectedRecordCollect!!,
                    menuList = menuList,
                    viewModel = viewModel,
                    modifier = Modifier.wrapContentHeight()
                )
            }

            Spacer(Modifier.size(10.dp))

            RecordList(
                recordList = recordList,
                viewModel = viewModel,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun SalesRecordingScreenPreview() {
    val recordList = MutableList(10) {
        SalesRecordingViewModelContract.DTO.Record(
            timestamp = "2024.07.08",
            totalPrice = "5${it},000",
            payment = "Point"
        )
    }
    val menuList = MutableList(10) {
        SalesRecordingViewModelContract.DTO.Menu(
            name = "test_${it}",
            count = "${it+1}",
            price = "9,500"
        )
    }
    val selectedRecord = SalesRecordingViewModelContract.DTO.Record(
        timestamp = "2024.07.08",
        totalPrice = "154,000",
        payment = "Point"
    )

    PPPTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            SalesRecordingScreen.RecordDetail(
                record = selectedRecord,
                menuList = menuList,
                viewModel = DummySalesRecordingViewModel,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )

            Spacer(Modifier.size(10.dp))

            SalesRecordingScreen.RecordList(
                recordList = recordList,
                viewModel = DummySalesRecordingViewModel,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )
        }
    }
}