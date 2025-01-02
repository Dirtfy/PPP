package com.dirtfy.ppp.ui.view.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.dirtfy.ppp.ui.controller.common.converter.common.PhoneNumberFormatConverter.formatPhoneNumber
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import kotlin.math.abs
import kotlin.math.max

object VisualTransformation {

    class CurrencyInputVisualTransformation: VisualTransformation {
        var original = ""
        val transformed get()  = StringFormatConverter.formatCurrency(
            original
        )

        private val offsetMapping = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // 문자열 젤 뒤를 offset 0으로 offset 거꾸로 세기
                val offsetComp = original.length - offset

                val remainComma = max((offsetComp + 2) / 3 - 1, 0)
                val totalComma = abs(original.length - transformed.length)
                val passedComma = totalComma - remainComma

                return offset + passedComma
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0

                for (i in 0..< offset) {
                    if (transformed[i] == ',')
                        continue
                    originalOffset++
                }

                return originalOffset
            }

        }

        override fun filter(text: AnnotatedString): TransformedText {
            original = text.text

            return TransformedText(
                AnnotatedString(text = transformed),
                offsetMapping
            )
        }

    }

    class PhoneNumberInputVisualTransformation: VisualTransformation {
        private var original = ""
        val transformed get() = formatPhoneNumber(original)

        private val offsetMapping = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalCount = 0

                for (element in transformed) {
                    if (originalCount == offset) break
                    transformedOffset++
                    if (element != '-') originalCount++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                var transformedCount = 0

                for (i in 0 until offset) {
                    if (i < transformed.length && transformed[i] != '-') {
                        originalOffset++
                    }
                    transformedCount++
                }
                return originalOffset
            }
        }

        override fun filter(text: AnnotatedString): TransformedText {
            original = text.text

            return TransformedText(
                text = AnnotatedString(text = transformed),
                offsetMapping = offsetMapping)
        }
    }

}