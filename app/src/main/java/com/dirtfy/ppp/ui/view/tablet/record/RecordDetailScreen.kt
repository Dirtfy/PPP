package com.dirtfy.ppp.ui.view.tablet.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail
import com.dirtfy.ppp.ui.view.phone.Component
import javax.inject.Inject

class RecordDetailScreen @Inject constructor(
    private val recordController: RecordController
) {

    @Composable
    fun Main(
        controller: RecordController = recordController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        ScreenContent(
            nowRecord = screenData.nowRecord,
            recordDetailList = screenData.recordDetailList,
            recordDetailListState = screenData.recordDetailListState,
            onDismissRequest = {
                controller.setRecordDetailListState(UiScreenState(UiState.COMPLETE))
            },
            onRetryClick = {
                controller.request { updateRecordDetailList() }
            }
        )

        Component.HandleUiStateDialog(
            uiState = screenData.nowRecordState,
            onDismissRequest = { controller.setNowRecordState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = null
        )
    }

    @Composable
    fun ScreenContent(
        nowRecord: UiRecord,
        recordDetailList: List<UiRecordDetail>,
        recordDetailListState: UiScreenState,
        onDismissRequest: () -> Unit,
        onRetryClick: () -> Unit
    ) {
        Surface(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.padding(15.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RecordDetailHead(nowRecord = nowRecord)
                    Component.HandleUiStateDialog(
                        uiState = recordDetailListState,
                        onDismissRequest = onDismissRequest,
                        onRetryAction = onRetryClick,
                        onComplete = {
                            if (recordDetailList.isNotEmpty())
                                RecordDetailList(recordDetailList = recordDetailList)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun RecordDetailHead(
        nowRecord: UiRecord
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = nowRecord.timestamp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = nowRecord.income, fontSize = 25.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = nowRecord.type)
                }
            }
        }
    }

    @Composable
    fun RecordDetailList(
        recordDetailList: List<UiRecordDetail>
    ) {
        Box {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                item {
                    Row {
                        Text(text = stringResource(R.string.name), modifier = Modifier.weight(1f))
                        Text(text = stringResource(R.string.price), modifier = Modifier.weight(1f))
                        Text(text = stringResource(R.string.count), modifier = Modifier.weight(1f))
                    }
                }
                items(recordDetailList) {
                    Row {
                        Text(text = it.name, modifier = Modifier.weight(1f))
                        Text(text = it.price, modifier = Modifier.weight(1f))
                        Text(text = it.count, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

    }
}