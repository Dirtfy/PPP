package com.dirtfy.ppp.ui.view.tablet.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode
import com.dirtfy.ppp.ui.view.Component
import javax.inject.Inject

class RecordScreen @Inject constructor(
    private val recordController: RecordController
) {
    @Inject
    lateinit var recordDetailScreen: RecordDetailScreen

    @Composable
    fun Main(
        controller: RecordController = recordController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        ScreenContent(
            selectedDatePair = screenData.dateRange,
            recordList = screenData.recordList,
            recordListState = screenData.recordListState,
            nowRecord = screenData.nowRecord,
            nowRecordState = screenData.nowRecordState,
            mode = screenData.mode,
            onSelectedDateChanged = { start, end ->
                controller.updateDateRange(start, end)
            },
            onItemClick = {
                controller.request{
                    setMode(UiRecordMode.Detail)
                    updateNowRecord(it)
                    updateRecordDetailList()
                }
            },
            onDismissRequest = {
                controller.setMode(UiRecordMode.Main)
            },
            onRetryClick = {
                controller.retryUpdateRecordList()
            },
            onRecordListFailDismissRequest = {
                controller.setRecordListState(UiScreenState(UiState.COMPLETE))
            }
        )

        Component.HandleUiStateDialog(
            uiState = screenData.nowRecordState,
            onDismissRequest = { controller.setNowRecordState(UiScreenState(UiState.COMPLETE)) },
            onRetryAction = null
        )

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenContent(
        selectedDatePair: Pair<Long?, Long?>,
        recordList: List<UiRecord>,
        recordListState: UiScreenState,
        nowRecord: UiRecord,
        nowRecordState: UiScreenState,
        mode: UiRecordMode,
        onSelectedDateChanged: (Long?, Long?) -> Unit,
        onItemClick: (UiRecord) -> Unit,
        onDismissRequest: () -> Unit,
        onRetryClick: () -> Unit,
        onRecordListFailDismissRequest: () -> Unit
    ) {
        Column {
            Row {
                Column(
                    modifier = Modifier.weight(0.3f)
                ) {
                    val dateRangePickerState = rememberDateRangePickerState(
                        initialSelectedStartDateMillis = selectedDatePair.first,
                        initialSelectedEndDateMillis = selectedDatePair.second
                    )

                    Button(
                        content = {
                            Text(text = "검색")
                        },
                        onClick = {
                            val start = dateRangePickerState.selectedStartDateMillis
                            val end = dateRangePickerState.selectedEndDateMillis

                            onSelectedDateChanged(start, end)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DateRangePicker(
                        state = dateRangePickerState,
                        headline = {
                            val start = dateRangePickerState.selectedStartDateMillis
                            val end = dateRangePickerState.selectedEndDateMillis

                            val startText = if (start == null) "시작일"
                            else StringFormatConverter.formatTimestampFromDay(start)

                            val endText = if (end == null) "종료일"
                            else StringFormatConverter.formatTimestampFromDay(end)

                            Text(text = "$startText ~ \n$endText")
                        }
                    )
                }


                Box(
                    modifier = Modifier.weight(0.7f)
                ) {
                    Component.HandleUiStateDialog(
                        uiState = recordListState,
                        onDismissRequest = onRecordListFailDismissRequest,
                        onRetryAction = onRetryClick,
                        onComplete = {
                            RecordList(
                                recordList = recordList,
                                onItemClick = onItemClick
                            )
                        }
                    )
                }

            }

            when(mode) {
                UiRecordMode.Detail -> {
                    RecordDetailDialog(
                        onDismissRequest = onDismissRequest
                    )
                }
                UiRecordMode.Main -> {}
            }
        }
    }

    @Composable
    fun RecordList(
        recordList: List<UiRecord>,
        onItemClick: (UiRecord) -> Unit
    ) {
        if (recordList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_list),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        else {
            LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
                items(recordList) {
                    Record(it, onItemClick)
                }
            }
        }
    }

    @Composable
    fun Record(
        record: UiRecord,
        onClick: (UiRecord) -> Unit
    ) {
        Card(
            modifier = Modifier.clickable {
                onClick(record)
            }.padding(10.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text=record.timestamp.substring(0,10), fontSize = 10.sp)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text=record.income, fontSize = 20.sp)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text=record.type, fontSize = 15.sp)
            }
        }
    }

    @Composable
    fun RecordDetailDialog(
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            recordDetailScreen.Main(
                onDismissRequest = onDismissRequest
            )
        }
    }
}