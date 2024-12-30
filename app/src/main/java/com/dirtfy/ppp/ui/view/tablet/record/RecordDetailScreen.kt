package com.dirtfy.ppp.ui.view.tablet.record

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecord
import com.dirtfy.ppp.ui.state.feature.record.atom.UiRecordDetail
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.view.Component
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject

class RecordDetailScreen @Inject constructor(
    private val recordController: RecordController
) {

    @Composable
    fun Main(
        onDismissRequest: () -> Unit,
        controller: RecordController = recordController
    ) {
        val screenData by controller.screenData.collectAsStateWithLifecycle()

        ScreenContent(
            nowRecord = screenData.nowRecord,
            recordDetailList = screenData.recordDetailList,
            recordDetailListState = screenData.recordDetailListState,
            onConfirm = { type ->
                controller.request { updateRecordType(type) }
            },
            onTypeInputDismissRequest = {
                controller.request { updateNowRecord(screenData.nowRecord) }
            },
            onDelete = {
                controller.request { deleteRecord(screenData.nowRecord) }
            },
            onDismissRequest = {
                controller.setRecordDetailListState(UiScreenState(UiState.COMPLETE))
                onDismissRequest()
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
        onConfirm: (String) -> Unit,
        onTypeInputDismissRequest: () -> Unit,
        onDelete: () -> Unit,
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
                    Box(
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Button(
                            onClick = {
                                onDismissRequest()
                                onDelete()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red, // 배경색
                            )
                        ) {
                            Text(text = "삭제")
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    RecordDetailHead(
                        nowRecord = nowRecord,
                        onConfirm = onConfirm,
                        onTypeInputDismissRequest = onTypeInputDismissRequest
                    )
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
        nowRecord: UiRecord,
        onConfirm: (String) -> Unit,
        onTypeInputDismissRequest: () -> Unit
    ) {
        var typeDialogShow by remember { mutableStateOf(false) }

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
                    Spacer(modifier = Modifier.size(5.dp))
                    Button(onClick = { typeDialogShow = true},
                        content = { Text(text = "수정") }
                    )
                }
            }
        }

        if (typeDialogShow) {
            TypeInputDialog(
                type = nowRecord.type,
                onConfirm = onConfirm,
                onDismissRequest = {
                    onTypeInputDismissRequest()
                    typeDialogShow = false
                }
            )
        }
    }

    @Composable
    fun TypeInput(
        type: String,
        onConfirm: (String) -> Unit
    ) {
        var newType by remember { mutableStateOf(type) }

        val parsedType = type.split("-")

        val accountNumber = if (parsedType.size > 1) type.split("-")[0].trim() else ""
        val userName = if (parsedType.size > 1) type.split("-")[1].trim() else ""
        var pointState by remember {
            mutableStateOf(UiPointUse(
                accountNumber = accountNumber,
                userName = userName
            ))
        }

        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract()
        ) {
            pointState = pointState.copy(accountNumber = it.contents?:"")
        }

        Column {
            Component.NamedRadioButton(
                name = "Card",
                selected = newType == "Card",
                onClick = { newType = "Card" })

            Component.NamedRadioButton(
                name = "Cash",
                selected = newType == "Cash",
                onClick = { newType = "Cash" })

            Row {
                RadioButton(
                    selected = !(newType == "Card" || newType == "Cash"),
                    onClick = {
                        newType = "${pointState.accountNumber}-${pointState.userName}"
                    })

                PointDataInput(
                    pointUse = pointState,
                    onAccountNumberChange = { pointState = pointState.copy(accountNumber = it) },
                    onUserNameChange = { pointState = pointState.copy(userName = it) },
                    onScanClick = {
                        scanLauncher.launch(
                            ScanOptions().setOrientationLocked(false)
                        )
                    }
                )
            }

            Button(
                content = { Text(text = "변경") },
                onClick = {
                    if (!(newType == "Card" || newType == "Cash"))
                        newType = "${pointState.accountNumber}-${pointState.userName}"
                    onConfirm(newType)
                })
        }

    }


    @Composable
    fun TypeInputDialog(
        type: String,
        onConfirm: (String) -> Unit,
        onDismissRequest: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier.wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TypeInput(
                        type = type,
                        onConfirm = {
                            onConfirm(it)
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun BarcodeIcon(
        onClick: () -> Unit
    ) {
        val barcodeIcon = Icons.Filled.Menu
        Icon(
            imageVector = barcodeIcon, contentDescription = barcodeIcon.name,
            modifier = Modifier
                .rotate(90f)
                .clickable {
                    onClick()
                }
        )
    }

    @Composable
    fun PointDataInput(
        pointUse: UiPointUse,
        onAccountNumberChange: (String) -> Unit,
        onUserNameChange: (String) -> Unit,
        onScanClick: () -> Unit
    ) {
        Column {
            TextField(
                value = pointUse.accountNumber,
                onValueChange = onAccountNumberChange,
                label = { Text(text = stringResource(R.string.account_number)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    BarcodeIcon(onClick = onScanClick)
                }
            )
            TextField(
                value = pointUse.userName,
                onValueChange = onUserNameChange,
                label = { Text(text = stringResource(R.string.issued_by)) }
            )
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