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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordMode
import com.dirtfy.ppp.ui.view.phone.Component
import javax.inject.Inject

class RecordScreen @Inject constructor(
    val recordController: RecordController
) {
    @Inject
    lateinit var recordDetailScreen: RecordDetailScreen

    @Composable
    fun Main(
        controller: RecordController = recordController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        Component.HandleUiStateDialog(
            uiState = screenData.nowRecordState,
            onDismissRequest = {}, //TODO Controller로 분리후 가능
            onRetryAction = {}//TODO Retry 에러날경우 클릭한 정보를 가지고 있어야 복구가 가능하다...
        )

        ScreenContent(
            searchClue = screenData.searchClue,
            recordList = screenData.recordList,
            recordListState = screenData.recordListState,
            nowRecord = screenData.nowRecord,
            nowRecordState = screenData.nowRecordState,
            mode = screenData.mode,
            onClueChanged = { controller.updateSearchClue(it) },
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
        Component.HandleUiStateDialog(
            uiState = recordListState,
            onDismissRequest = {}, onRetryAction = {  },// TODO Retry 어떻게 할지 생각 필요...
            onComplete = {
                RecordList(
                    recordList = recordList,
                    onItemClick = onItemClick
                )
            }
        )
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
    fun RecordDetailDialog(
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            recordDetailScreen.Main()
        }
    }
}