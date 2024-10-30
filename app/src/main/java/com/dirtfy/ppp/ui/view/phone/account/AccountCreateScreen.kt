package com.dirtfy.ppp.ui.view.phone.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import javax.inject.Inject

class AccountCreateScreen @Inject constructor(
    val accountCreateController: AccountCreateController
) {

    @Composable
    fun Main(
        controller: AccountCreateController = accountCreateController,
        onAccountCreate: (UiNewAccount) -> Unit = {},
    ) {
        val screen by controller.screenData.collectAsStateWithLifecycle()

        ScreenContent(
            newAccount = screen.newAccount,
            onValueChange = { controller.request { updateNewAccount(it) } },
            onAutoGenerateClick = { controller.request { setRandomValidAccountNumberToNewAccount() } },
            onCreateClick = {
                controller.request { addAccount(screen.newAccount) }
                onAccountCreate(screen.newAccount)
            }
        )
    }

    @Composable
    fun ScreenContent(
        newAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit,
        onAutoGenerateClick: () -> Unit,
        onCreateClick: () -> Unit
    ) {
        Box(modifier = Modifier.wrapContentHeight().background(Color.White).padding(15.dp)) {
            Column(horizontalAlignment = Alignment.End) {
                Card {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "New Account",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        AccountInput(
                            nowAccount = newAccount,
                            onValueChange = onValueChange,
                            onAutoGenerateClick = onAutoGenerateClick
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        CreateButton(
                            onClick = onCreateClick
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun AccountInput(
        nowAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit,
        onAutoGenerateClick: () -> Unit
    ) {
        Column {
            AccountNumberInput(
                nowAccount = nowAccount,
                onValueChange = onValueChange,
                onAutoGenerateClick = onAutoGenerateClick
            )
            Spacer(modifier = Modifier.size(10.dp))

            AccountNameInput(
                nowAccount = nowAccount,
                onValueChange = onValueChange
            )
            Spacer(modifier = Modifier.size(10.dp))

            AccountPhoneNumberInput(
                nowAccount = nowAccount,
                onValueChange = onValueChange
            )
        }
    }

    @Composable
    fun AccountNumberInput(
        nowAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit,
        onAutoGenerateClick: () -> Unit
    ) {
        TextField(
            label = { Text(text = "account number") },
            value = nowAccount.number,
            onValueChange = {
                onValueChange(nowAccount.copy(number = it))
            },
            trailingIcon = {
                AccountNumberGenerate(onAutoGenerateClick)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
    @Composable
    fun AccountNumberGenerate(
        onClick: () -> Unit
    ) {
        val randomIcon = Icons.Filled.Refresh
        Icon(
            imageVector = randomIcon,
            contentDescription = randomIcon.name,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }

    @Composable
    fun AccountNameInput(
        nowAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit
    ) {
        TextField(
            label = { Text(text = "account name") },
            value = nowAccount.name,
            onValueChange = {
                onValueChange(nowAccount.copy(name = it))
            }
        )
    }

    private val phoneNumberVisualTransformation = VisualTransformation { text ->
        val cleaned = text.text.replace("-", "")
        val areaCodes = arrayOf("031", "032", "033", "041", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064")
        val newText = StringBuilder()

        fun formatNumber(prefix: String, startIndex: Int, middleIndex: Int, endIndex: Int): String {
            return when {
                cleaned.length <= startIndex -> cleaned
                cleaned.length in (startIndex + 1)..middleIndex -> "$prefix-${cleaned.substring(startIndex)}"
                cleaned.length in (middleIndex + 1)..endIndex -> "$prefix-${cleaned.substring(startIndex, middleIndex)}-${cleaned.substring(middleIndex)}"
                else -> "$prefix-${cleaned.substring(startIndex, startIndex + 4)}-${cleaned.substring(startIndex + 4)}"
            }
        }

        newText.append(
            when {
                cleaned.startsWith("02") -> formatNumber("02", 2, 5, 9)
                cleaned.startsWith("010") -> formatNumber("010", 3, 7, 11)
                areaCodes.any { cleaned.startsWith(it) } -> formatNumber(cleaned.substring(0, 3), 3, 6, 10)
                else -> cleaned
            }
        )

        val transformedText = newText.toString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalCount = 0

                for (i in 0 until transformedText.length) {
                    if (originalCount == offset) {
                        break
                    }
                    transformedOffset++
                    if (transformedText[i] != '-') originalCount++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {//커서 위치 옮길 때
                var originalOffset = 0
                var transformedCount = 0

                for (i in 0 until offset) {
                    if (i < transformedText.length) {
                        if (transformedText[i] != '-') {
                            originalOffset++
                        }
                        transformedCount++
                    }
                }
                return originalOffset
            }
        }
        TransformedText(AnnotatedString(transformedText), offsetMapping)
    }

    @Composable
    fun AccountPhoneNumberInput(
        nowAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit
    ) {
        TextField(
            label = { Text(text = "phone number") },
            value = nowAccount.phoneNumber,
            onValueChange = {
                onValueChange(nowAccount.copy(phoneNumber = it))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            visualTransformation = phoneNumberVisualTransformation
        )
    }

    @Composable
    fun CreateButton(
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "add")
        }
    }

}