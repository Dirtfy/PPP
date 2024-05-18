package com.dirtfy.ppp.accounting.accountRecording.barcoding.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.accounting.accountRecording.barcoding.model.BarCodeGenerator
import com.dirtfy.ppp.accounting.accountRecording.barcoding.model.BarcodeData
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.common.Generator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BarCodeViewModel: ViewModel(){

    private val generator: Generator<BarcodeData, ImageBitmap> = BarCodeGenerator

    private val _barCode: MutableStateFlow<ImageBitmap> =
        MutableStateFlow(ImageBitmap(400, 400))
    val barCode: StateFlow<ImageBitmap>
        get() = _barCode

    fun generate(inputData: AccountData) {
        _barCode.value = generator.generate(
            BarcodeData(
                inputData.accountID?: "null"
            )
        )
    }

}