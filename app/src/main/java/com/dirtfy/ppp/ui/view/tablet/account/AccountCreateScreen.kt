package com.dirtfy.ppp.ui.view.tablet.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.state.feature.account.atom.UiNewAccount
import javax.inject.Inject

class AccountCreateScreen @Inject constructor(
    val accountCreateController: AccountCreateController
){

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
        Surface(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.padding(15.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Card {
                        Box(modifier = Modifier.padding(10.dp)) {
                            AccountInput(
                                nowAccount = newAccount,
                                onValueChange = onValueChange,
                                onAutoGenerateClick = onAutoGenerateClick
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    CreateButton(onClick = onCreateClick)
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
            }
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
            }
        )
    }

    @Composable
    fun CreateButton(
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick
        ) {
            Text(text = "add")
        }
    }

}