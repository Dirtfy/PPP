package com.dirtfy.ppp.ui.presenter.controller.common

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun formatCurrency(price: Int): String {
        return DecimalFormat("#,###").format(price)
    }

    fun parseCurrency(price: String): Int {
        return price.split(",").joinToString(separator = "").toInt()
    }

    private fun formatTimestamp(timestamp: Long, format: String): String {
        val date = Date(timestamp)

        val formatter = SimpleDateFormat(
            format, Locale.getDefault()
        )

        return formatter.format(date)
    }
    private fun parseTimestamp(dateString: String, format: String): Long {
        val formatter = SimpleDateFormat(
            format, Locale.getDefault()
        )

        val date: Date = formatter.parse(dateString)
            ?: throw IllegalArgumentException("Invalid date format")

        return date.time
    }

    private val dayFormat = "yyyy.MM.dd"
    private val minuteFormat = "yyyy.MM.dd HH:mm"
    private val secondFormat = "yyyy.MM.dd HH:mm:ss"
    private val millisFormat = "yyyy.MM.dd HH:mm:ss.SSS"

    fun formatTimestampFromDay(timestamp: Long): String =
        formatTimestamp(timestamp, dayFormat)
    fun parseTimestampFromDay(dateString: String): Long =
        parseTimestamp(dateString, dayFormat)

    fun formatTimestampFromMinute(timestamp: Long): String =
        formatTimestamp(timestamp, minuteFormat)
    fun parseTimestampFromMinute(dateString: String): Long =
        parseTimestamp(dateString, minuteFormat)

    fun formatTimestampFromSecond(timestamp: Long): String =
        formatTimestamp(timestamp, secondFormat)
    fun parseTimestampFromSecond(dateString: String): Long =
        parseTimestamp(dateString, secondFormat)

    fun formatTimestampFromMillis(timestamp: Long): String =
        formatTimestamp(timestamp, millisFormat)
    fun parseTimestampFromMillis(dateString: String): Long =
        parseTimestamp(dateString, millisFormat)
}