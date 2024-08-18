package com.dirtfy.ppp.viewmodel.accounting.managing.barcoding

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.dirtfy.ppp.common.Generator
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account
import com.dirtfy.ppp.model.accounting.managing.barcoding.BarCodeGenerator
import com.dirtfy.ppp.model.accounting.managing.barcoding.BarcodeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BarCodeViewModel: ViewModel(){

    private val generator: Generator<BarcodeData, ImageBitmap> = BarCodeGenerator

    private val _barCode: MutableStateFlow<ImageBitmap> =
        MutableStateFlow(ImageBitmap(400, 400))
    val barCode: StateFlow<ImageBitmap>
        get() = _barCode

    fun generate(inputData: Account) {
        _barCode.value = generator.generate(
            BarcodeData(
                inputData.accountID?: "null"
            )
        )
    }

}