package com.dirtfy.ppp.accounting.accountRecording.barcoding.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.dirtfy.ppp.MainActivity
import com.dirtfy.ppp.PPPScreen
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.common.Generator
import com.google.zxing.BarcodeFormat

import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object QRCodeGenerator: Generator<AccountData, ImageBitmap> {

    override fun generate(inputData: AccountData): ImageBitmap {
        val barcodeEncoder = BarcodeEncoder()

        val prefix = "${MainActivity.Const.deepLinkScheme}://" +
                "${MainActivity.Const.deepLinkHost}/"
        val contents = PPPScreen.AccountRecord.buildArgumentString(inputData)

        return barcodeEncoder.encodeBitmap(
            prefix + contents,
            BarcodeFormat.QR_CODE,
            400, 400).asImageBitmap()
    }


}