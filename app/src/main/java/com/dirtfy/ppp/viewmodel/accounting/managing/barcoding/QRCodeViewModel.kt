package com.dirtfy.ppp.viewmodel.accounting.managing.barcoding

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.model.Generator
import com.dirtfy.ppp.model.accounting.accounting.AccountData
import com.dirtfy.ppp.model.accounting.managing.barcoding.BarcodeData
import com.dirtfy.ppp.model.accounting.managing.barcoding.QRCodeGenerator
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