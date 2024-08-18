package com.dirtfy.ppp.model.accounting.managing.barcoding

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.dirtfy.ppp.common.Generator
import com.dirtfy.ppp.view.TestMainActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


object QRCodeGenerator: Generator<BarcodeData, ImageBitmap> {

    private val encoder = BarcodeEncoder()

    override fun generate(inputData: BarcodeData): ImageBitmap {
        val contents = "${TestMainActivity.Const.deepLinkScheme}://" +
                "${TestMainActivity.Const.deepLinkHost}/" +
                inputData.accountID

        return encoder.encodeBitmap(
            contents,
            BarcodeFormat.QR_CODE,
            400, 400).asImageBitmap()
    }


}