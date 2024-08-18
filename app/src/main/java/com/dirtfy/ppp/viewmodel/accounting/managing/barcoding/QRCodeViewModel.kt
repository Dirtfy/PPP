package com.dirtfy.ppp.viewmodel.accounting.managing.barcoding

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.common.Generator
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account
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

    fun generate(inputData: Account) {
        _QRCode.value = generator.generate(
            BarcodeData(
                inputData.accountID?: "null"
            )
        )
    }

}