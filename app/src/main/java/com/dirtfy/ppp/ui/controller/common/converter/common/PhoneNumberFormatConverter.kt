package com.dirtfy.ppp.ui.controller.common.converter.common

object PhoneNumberFormatConverter {
    private val areaCodes = arrayOf(
        "031", "032", "033",
        "041", "043", "044",
        "051", "052", "053",
        "054", "055", "061",
        "062", "063", "064"
    )

    private fun formatNumber(
        cleaned: String,
        prefix: String,
        startIndex: Int,
        middleIndex: Int,
        endIndex: Int
    ): String {
        return when {
            cleaned.length <= startIndex -> cleaned

            cleaned.length in (startIndex + 1)..middleIndex
                -> "$prefix-${cleaned.substring(startIndex)}"

            cleaned.length in (middleIndex + 1)..endIndex
                -> prefix +
                    "-${cleaned.substring(startIndex, middleIndex)}" +
                    "-${cleaned.substring(middleIndex)}"

            else
                -> prefix +
                    "-${cleaned.substring(startIndex, startIndex + 4)}" +
                    "-${cleaned.substring(startIndex + 4)}"
        }
    }

    fun formatPhoneNumber(input: String): String {
        val cleaned = input.replace("-", "")

        return when {
            cleaned.startsWith("02")
                -> formatNumber(cleaned, "02", 2, 5, 9)
            cleaned.startsWith("010")
                -> formatNumber(cleaned, "010", 3, 7, 11)
            areaCodes.any { cleaned.startsWith(it) }
                -> formatNumber(
                cleaned, cleaned.substring(0, 3),
                3, 6, 10
                )
            else -> cleaned
        }
    }
}
