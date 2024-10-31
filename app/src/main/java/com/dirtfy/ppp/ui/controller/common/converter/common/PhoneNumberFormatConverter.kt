package com.dirtfy.ppp.ui.controller.common.converter.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

object PhoneNumberFormatConverter {
    private val areaCodes = arrayOf("031", "032", "033", "041", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064")

    fun formatPhoneNumber(input: String): TransformedText {
        val cleaned = input.replace("-", "")
        val newText = StringBuilder()

        fun formatNumber(prefix: String, startIndex: Int, middleIndex: Int, endIndex: Int): String {
            return when {
                cleaned.length <= startIndex -> cleaned
                cleaned.length in (startIndex + 1)..middleIndex -> "$prefix-${cleaned.substring(startIndex)}"
                cleaned.length in (middleIndex + 1)..endIndex -> "$prefix-${cleaned.substring(startIndex, middleIndex)}-${cleaned.substring(middleIndex)}"
                else -> "$prefix-${cleaned.substring(startIndex, startIndex + 4)}-${cleaned.substring(startIndex + 4)}"
            }
        }

        newText.append(
            when {
                cleaned.startsWith("02") -> formatNumber("02", 2, 5, 9)
                cleaned.startsWith("010") -> formatNumber("010", 3, 7, 11)
                areaCodes.any { cleaned.startsWith(it) } -> formatNumber(cleaned.substring(0, 3), 3, 6, 10)
                else -> cleaned
            }
        )

        val transformedText = newText.toString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalCount = 0

                for (i in 0 until transformedText.length) {
                    if (originalCount == offset) break
                    transformedOffset++
                    if (transformedText[i] != '-') originalCount++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                var transformedCount = 0

                for (i in 0 until offset) {
                    if (i < transformedText.length && transformedText[i] != '-') {
                        originalOffset++
                    }
                    transformedCount++
                }
                return originalOffset
            }
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
