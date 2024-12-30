package com.dirtfy.ppp.ui.view

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dirtfy.ppp.R
import com.dirtfy.ppp.common.exception.ExceptionRetryHandling
import com.dirtfy.ppp.ui.controller.common.converter.common.PhoneNumberFormatConverter.formatPhoneNumber
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import kotlin.math.max
import kotlin.math.min

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
                            keyboardType = if (isNumber) KeyboardType.NumberPassword else KeyboardType.Text
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

    @Composable
    fun NamedRadioButton(
        name: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Text(text = name)
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

    class CurrencyInputVisualTransformation: VisualTransformation {
        var originalText = ""
        val trimmedText get(): String {
            return if (!(originalText.toList().distinct().size == 1 &&
                        originalText[0] == '0'))
                originalText.trimStart { it == '0' }
            else "0"
        }
        val formatedText get(): String {
            return if (trimmedText != "")
                StringFormatConverter.formatCurrency(
                    trimmedText.toInt()
                )
            else ""
        }

        val offsetMapping = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val trimmedLength = originalText.length - trimmedText.length

                if (offset < trimmedLength)
                    return trimmedText.length

                var formatedOffset = 0

                val trimmedOffset = offset - trimmedLength
                for (i in 0..< trimmedOffset) {
                    if (formatedText[i] == ',')
                        formatedOffset++
                    formatedOffset++
                }

                return formatedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                val trimmedLength = originalText.length - trimmedText.length

                var originalOffset = trimmedLength

                for (i in 0..< offset) {
                    if (formatedText[i] == ',')
                        continue
                    originalOffset++
                }

                return originalOffset
            }

        }

        override fun filter(text: AnnotatedString): TransformedText {
            originalText = text.text

            return TransformedText(
                AnnotatedString(text = formatedText),
                offsetMapping
            )
        }

    }
    private fun getPhoneNumberTransfomred(input: String): TransformedText {
        val transformedText = formatPhoneNumber(input)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalCount = 0

                for (element in transformedText) {
                    if (originalCount == offset) break
                    transformedOffset++
                    if (element != '-') originalCount++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                var transformedCount = 0

                for (i in 0 until offset) {
                    if (i < transformedText.length && transformedText[i] != '-') {
                        originalOffset++
                    }
                    transformedCount++
                }
                return originalOffset
            }
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}