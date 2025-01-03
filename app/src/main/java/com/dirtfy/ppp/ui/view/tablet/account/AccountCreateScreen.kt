package com.dirtfy.ppp.ui.view.tablet.account

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.R
import com.dirtfy.ppp.ui.controller.feature.account.AccountController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import com.dirtfy.ppp.ui.view.common.Component
import com.dirtfy.ppp.ui.view.common.VisualTransformation
import javax.inject.Inject

class AccountCreateScreen @Inject constructor(
    private val accountController: AccountController
) {

    @Composable
    fun Main(
        controller: AccountController = accountController,
    ) {
        val screen by controller.screenData.collectAsStateWithLifecycle()


        ScreenContent(
            newAccount = screen.newAccount,
            onValueChange = { controller.request { updateNewAccount(it) } },
            onAutoGenerateClick = { controller.request { setRandomValidAccountNumberToNewAccount() } },
            onCreateClick = {
                controller.request {
                    addAccount()
                }
            }
        )

        Component.HandleUiStateDialog(
            uiState = screen.addAccountState,
            onDismissRequest = {controller.setAddAccountState(UiScreenState(UiState.COMPLETE))},
            onRetryAction = { controller.request { addAccount() } }
        )

        Component.HandleUiStateDialog(
            uiState = screen.numberGeneratingState,
            onDismissRequest = {controller.setNumberGeneratingState(UiScreenState(UiState.COMPLETE))},
            onRetryAction = {controller.request { setRandomValidAccountNumberToNewAccount() }}
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
                            text = stringResource(R.string.new_account),
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
            label = { Text(text = stringResource(R.string.account_number)) },
            value = nowAccount.number,
            onValueChange = {
                onValueChange(nowAccount.copy(number = it))
            },
            trailingIcon = {
                AccountNumberGenerate(onAutoGenerateClick)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
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
            label = { Text(text = stringResource(R.string.account_name)) },
            value = nowAccount.name,
            onValueChange = {
                onValueChange(nowAccount.copy(name = it))
            }
        )
    }

    @Composable
    fun AccountPhoneNumberInput(
        nowAccount: UiNewAccount,
        onValueChange: (UiNewAccount) -> Unit
    ) {
        TextField(
            label = { Text(text = stringResource(R.string.phone_number)) },
            value = nowAccount.phoneNumber,
            onValueChange = {
                onValueChange(nowAccount.copy(phoneNumber = it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = VisualTransformation.PhoneNumberInputVisualTransformation()
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
            Text(text = stringResource(R.string.add))
        }
    }
}