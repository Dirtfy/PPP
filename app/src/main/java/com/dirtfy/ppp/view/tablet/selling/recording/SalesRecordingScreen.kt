package com.dirtfy.ppp.view.tablet.selling.recording

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.view.sales.recording.SalesRecordingScreenContract
import com.dirtfy.ppp.contract.viewmodel.SalesRecordingContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object SalesRecordingScreen : SalesRecordingScreenContract.API {
    @Composable
    override fun RecordList(
        recordList: List<SalesRecordingContract.DTO.Record>,
        user: User,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = modifier
        ) {
            items(recordList) {
                RecordItem(
                    record = it,
                    user = user,
                    modifier = Modifier
                )
            }
        }
    }

    @Composable
    override fun RecordItem(
        record: SalesRecordingContract.DTO.Record,
        user: User,
        modifier: Modifier
    ) {
        ListItem(
            overlineContent = { Text(text = record.timestamp) },
            headlineContent = { Text(text = record.totalPrice) },
            supportingContent = { Text(text = record.payment) }
        )
    }

    @Composable
    override fun RecordDetail(
        record: SalesRecordingContract.DTO.Record,
        menuList: List<SalesRecordingContract.DTO.Menu>,
        user: User,
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
        viewModel: SalesRecordingContract.API,
        user: User,
        modifier: Modifier
    ) {
        val recordList by viewModel.recordList.collectAsStateWithLifecycle()

        val selectedRecordCollect by viewModel.selectedRecord.collectAsStateWithLifecycle()
        val selectedRecord = selectedRecordCollect

        val menuList by viewModel.selectedRecordMenuList.collectAsStateWithLifecycle()

        Row(
            modifier = modifier
        ) {
            RecordList(
                recordList = recordList,
                user = user,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            Spacer(Modifier.size(10.dp))

            if (selectedRecord != null) {
                RecordDetail(
                    record = selectedRecord,
                    menuList = menuList,
                    user = user,
                    modifier = Modifier.widthIn(200.dp, 300.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
fun SalesRecordingScreenPreview() {
    val recordList = MutableList(10) {
        SalesRecordingContract.DTO.Record(
            timestamp = "2024.07.08",
            totalPrice = "5${it},000",
            payment = "Point"
        )
    }
    val menuList = MutableList(10) {
        SalesRecordingContract.DTO.Menu(
            name = "test_${it}",
            count = "${it+1}",
            price = "9,500"
        )
    }
    val selectedRecord = SalesRecordingContract.DTO.Record(
        timestamp = "2024.07.08",
        totalPrice = "154,000",
        payment = "Point"
    )

    PPPTheme {
        Row(
            modifier = Modifier
        ) {
            SalesRecordingScreen.RecordList(
                recordList = recordList,
                user = DummyUser,
                modifier = Modifier.widthIn(600.dp, 800.dp)
            )

            Spacer(Modifier.size(10.dp))

            SalesRecordingScreen.RecordDetail(
                record = selectedRecord,
                menuList = menuList,
                user = DummyUser,
                modifier = Modifier.widthIn(200.dp, 300.dp)
            )
        }
    }
}