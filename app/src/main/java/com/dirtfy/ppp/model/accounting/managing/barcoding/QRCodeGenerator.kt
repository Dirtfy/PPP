package com.dirtfy.ppp.model.accounting.managing.barcoding

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.dirtfy.ppp.model.Generator
import com.dirtfy.ppp.view.MainActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


object QRCodeGenerator: Generator<BarcodeData, ImageBitmap> {

    private val encoder = BarcodeEncoder()

    override fun generate(inputData: BarcodeData): ImageBitmap {
        val contents = "${MainActivity.Const.deepLinkScheme}://" +
                "${MainActivity.Const.deepLinkHost}/" +
                inputData.accountID

        return encoder.encodeBitmap(
            contents,
            BarcodeFormat.QR_CODE,
            400, 400).asImageBitmap()
    }


}