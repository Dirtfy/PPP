package com.dirtfy.ppp.accounting.accountRecording.barcoding.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.accounting.accountRecording.barcoding.model.BarcodeData
import com.dirtfy.ppp.accounting.accountRecording.barcoding.model.QRCodeGenerator
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.common.Generator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QRCodeViewModel: ViewModel(){

    private val generator: Generator<BarcodeData, ImageBitmap> = QRCodeGenerator

    private val _QRCode: MutableStateFlow<ImageBitmap> =
        MutableStateFlow(ImageBitmap(400, 400))
    val QRCode: StateFlow<ImageBitmap>
        get() = _QRCode

    fun generate(inputData: AccountData) {
        _QRCode.value = generator.generate(
            BarcodeData(
                inputData.accountID?: "null"
            )
        )
    }

}