package com.dirtfy.ppp.ui.view.phone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.R
import com.dirtfy.ppp.common.exception.ExceptionRetryHandling
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState

object Component {

    @Composable
    fun SearchBar(
        searchClue: String,
        onClueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        isNumber: Boolean = false,
        placeholder: String = "",
        trailingContent: @Composable () -> Unit = {},
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Card(
                modifier = Modifier.padding(20.dp, 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .wrapContentWidth()
                ) {
                    val searchIcon = Icons.Filled.Search
                    Icon(imageVector = searchIcon, contentDescription = searchIcon.name)
                    TextField(
                        value = searchClue,
                        onValueChange = onClueChanged,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text(text = placeholder) },
                        singleLine = true,
                        modifier = Modifier.widthIn(200.dp, 400.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text
                        )
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    trailingContent()
                }
            }
        }
    }

    @Composable
    fun Loading() {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    fun Fail(
        onDismissRequest: () -> Unit,
        errorException: Throwable?,
        onRetryAction: (() -> Unit)? = null
    ) {
        val shouldRetry = errorException?.let { ExceptionRetryHandling.isRetryable(it) } ?: false

        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            dismissButton = {
                if (shouldRetry) {
                    Button(onClick = { onRetryAction?.invoke() }) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            },
            title = { Text(text = errorException?.message ?: stringResource(R.string.unknown_error)) }
        )
    }

    @Composable
    fun HandleUiStateDialog(
        uiState: UiScreenState,
        onDismissRequest: () -> Unit,
        onRetryAction: (() -> Unit)? = null,
        onComplete: @Composable () -> Unit = {}
    ) {
        when (uiState.state) {
            UiState.LOADING -> {
                Loading()
            }
            UiState.COMPLETE -> onComplete()
            UiState.FAIL -> {
                Fail(
                    onDismissRequest = onDismissRequest ?: {},
                    errorException = uiState.errorException,
                    onRetryAction = onRetryAction ?: {}
                )
            }
        }
    }
}