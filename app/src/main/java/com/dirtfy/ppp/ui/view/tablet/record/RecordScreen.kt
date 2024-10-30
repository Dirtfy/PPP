package com.dirtfy.ppp.ui.view.tablet.record

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
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordViewModel
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode

object RecordScreen {

    @Composable
    fun Main(
        controller: RecordController = viewModel<RecordViewModel>()
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = controller) {
            //controller.updateRecordList()
        }

        ScreenContent(
            searchClue = screenData.searchClue,
            recordList = screenData.recordList,
            recordListState = screenData.recordListState,
            nowRecord = screenData.nowRecord,
            nowRecordState = screenData.nowRecordState,
            mode = screenData.mode,
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
                //controller.request { updateRecordList() }
            }
        )

    }

    @Composable
    fun ScreenContent(
        searchClue: String,
        recordList: List<UiRecord>,
        recordListState: UiScreenState,
        nowRecord: UiRecord,
        nowRecordState: UiScreenState,
        mode: UiRecordMode,
        onClueChanged: (String) -> Unit,
        onItemClick: (UiRecord) -> Unit,
        onDismissRequest: () -> Unit,
        onRetryClick: () -> Unit
    ) {
        Column {
//            Component.SearchBar(
//                searchClue = searchClue,
//                onClueChanged = onClueChanged,
//                placeholder = "record date"
//            )
            RecordListState(
                recordList = recordList,
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
        recordList: List<UiRecord>,
        recordListState: UiScreenState,
        onItemClick: (UiRecord) -> Unit,
        onRetryClick: () -> Unit
    ) {
        when(recordListState.state) {
            UiState.COMPLETE -> {
                RecordList(
                    recordList = recordList,
                    onItemClick = onItemClick
                )
            }
            UiState.LOADING -> {
                RecordListLoading()
            }
            UiState.FAIL -> {
                RecordListLoadFail(
                    failMessage = recordListState.failMessage,
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
        failMessage: String?,
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
            title = { Text(text = failMessage ?: "unknown error") }
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