package com.dirtfy.ppp.test.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TestMainScreen(
    navigateToAccountTest: () -> Unit,
    navigateToRecordTest: () -> Unit,
    navigateToMenuTest: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = navigateToAccountTest) {
            Text(text = "account test")
        }
        Button(onClick = navigateToRecordTest) {
            Text(text = "account record test")
        }
        Button(onClick = navigateToMenuTest) {
            Text(text = "menu test")
        }
    }
}