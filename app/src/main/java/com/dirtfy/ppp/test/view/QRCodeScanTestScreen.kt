package com.dirtfy.ppp.test.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun ScanResult(contents: String) {
    Text(text = contents)
}

@Composable
fun QRCodeScanTest() {
    Column {
        val contents = remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(contract = ScanContract()) {
            contents.value = it.contents
        }
        Button(onClick = {
            launcher.launch(ScanOptions())
        }) {
            Text(text = "scan")
        }

        ScanResult(contents.value)
    }
}