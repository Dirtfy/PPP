package com.dirtfy.ppp.accounting.accountRecording.barcoding.model

import android.graphics.Bitmap
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.google.zxing.BarcodeFormat

import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object QRCodeGenerator {

    fun convert(data: AccountData): Bitmap {
        val barcodeEncoder = BarcodeEncoder()

        val contents = Json.encodeToString(data)

        return barcodeEncoder.encodeBitmap(
            contents,
            BarcodeFormat.QR_CODE,
            400, 400)
    }


}