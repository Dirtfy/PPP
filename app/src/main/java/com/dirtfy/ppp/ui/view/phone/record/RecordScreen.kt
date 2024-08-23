package com.dirtfy.ppp.ui.view.phone.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiRecord
import com.dirtfy.ppp.ui.dto.UiRecordMode
import com.dirtfy.ppp.ui.presenter.controller.record.RecordController
import com.dirtfy.ppp.ui.presenter.viewmodel.record.RecordViewModel
import com.dirtfy.ppp.ui.view.phone.Component

object RecordScreen {

    @Composable
    fun Main(
        controller: RecordController = viewModel<RecordViewModel>()
    ) {
        val recordListState by controller.recordList.collectAsStateWithLifecycle()
        val searchClue by controller.searchClue.collectAsStateWithLifecycle()
        val nowRecord by controller.nowRecord.collectAsStateWithLifecycle()
        val mode by controller.mode.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            controller.updateRecordList()
        }

        ScreenContent(
            searchClue = searchClue,
            recordListState = recordListState,
            nowRecord = nowRecord,
            mode = mode,
            onClueChanged = { controller.updateSearchClue(it) },
            onItemClick = {
                controller.run{
                    updateNowRecord(it)
                    setMode(UiRecordMode.Detail)
                }
            },
            onDismissRequest = {
                controller.setMode(UiRecordMode.Main)
            },
            onRetryClick = {
                controller.updateRecordList()
            }
        )

    }

    @Composable
    fun ScreenContent(
        searchClue: String,
        recordListState: FlowState<List<UiRecord>>,
        nowRecord: UiRecord,
        mode: UiRecordMode,
        onClueChanged: (String) -> Unit,
        onItemClick: (UiRecord) -> Unit,
        onDismissRequest: () -> Unit,
        onRetryClick: () -> Unit
    ) {
        Column {
            Component.SearchBar(
                searchClue = searchClue,
                onClueChanged = onClueChanged,
                placeholder = "record date"
            )
            RecordListState(
                recordListState = recordListState,
                onItemClick = onItemClick,
                onRetryClick = onRetryClick
            )

            when(mode) {
                UiRecordMode.Detail -> {
                    RecordDetailDialog(
                        nowAccount = nowRecord,
                        onDismissRequest = onDismissRequest
                    )
                }
                UiRecordMode.Main -> {}
            }
        }
    }

    @Composable
    fun RecordListState(
        recordListState: FlowState<List<UiRecord>>,
        onItemClick: (UiRecord) -> Unit,
        onRetryClick: () -> Unit
    ) {
        when(recordListState) {
            is FlowState.Success -> {
                val recordList = recordListState.data
                RecordList(
                    recordList = recordList,
                    onItemClick = onItemClick
                )
            }
            is FlowState.Loading -> {
                RecordListLoading()
            }
            is FlowState.Failed -> {
                val throwable = recordListState.throwable
                RecordListLoadFail(
                    throwable = throwable,
                    onRetryClick = onRetryClick
                )
            }
        }
    }

    @Composable
    fun RecordList(
        recordList: List<UiRecord>,
        onItemClick: (UiRecord) -> Unit
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
            items(recordList) {
                ListItem(
                    overlineContent = { Text(text = it.timestamp.substring(0,10)) },
                    headlineContent = { Text(text = it.income) },
                    supportingContent = { Text(text = it.type)},
                    modifier = Modifier.clickable {
                        onItemClick(it)
                    }
                )
            }
        }
    }

    @Composable
    fun RecordListLoading() {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun RecordListLoadFail(
        throwable: Throwable,
        onRetryClick: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = { }) {
                    Text(text = "Cancel")
                }
            },
            dismissButton = {
                Button(onClick = onRetryClick) {
                    Text(text = "Retry")
                }
            },
            title = { Text(text = throwable.message?: "unknown error") }
        )
    }

    @Composable
    fun RecordDetailDialog(
        nowAccount: UiRecord,
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            RecordDetailScreen.Main(firstRecord = nowAccount)
        }
    }
}