package com.dirtfy.ppp.accounting.accountRecording.barcoding.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.dirtfy.ppp.MainActivity
import com.dirtfy.ppp.common.Generator
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object BarCodeGenerator: Generator<BarcodeData, ImageBitmap> {

    private val encoder = BarcodeEncoder()

    override fun generate(inputData: BarcodeData): ImageBitmap {
        val contents = inputData.accountID

        return encoder.encodeBitmap(
            contents,
            BarcodeFormat.EAN_8,
            400, 400).asImageBitmap()
    }
}