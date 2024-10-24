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

    fun formatTimestampFromDay(time: Long): String {
        val dateFormat = "yyyy.MM.dd"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.format(date)
    }

    fun parseTimestampFromDay(time: String): Long {
        val dateFormat = "yyyy.MM.dd"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.parse(time)?.time ?: -1L
    }

    fun formatTimestampFromMinute(time: Long): String {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.format(date)
    }

    fun parseTimestampFromMinute(time: String): Long {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.parse(time)?.time ?: -1L
    }

    fun formatTimestampFromSecond(time: Long): String {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        val formed = simpleDateFormat.let {
            it.parse(it.format(date))?.time!!
        }
        val sec = time - formed

        return simpleDateFormat.format(date) + sec.toString()
    }

    fun parseTimestampFromSecond(time: String): Long {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)
        val underMinute = time.substring(16).toLong()

        return simpleDateFormat.parse(time.substring(0, 16))?.time?.plus(underMinute) ?: -1L
    }

    fun formatTimestampFromMillis(timestamp: Long): String {
        val date = Date(timestamp)

        val formatter = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS",
            Locale.getDefault()
        )

        return formatter.format(date)
    }

    fun parseTimestampFromMillis(dateString: String): Long {
        val formatter = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS",
            Locale.getDefault()
        )

        val date: Date = formatter.parse(dateString)
            ?: throw IllegalArgumentException("Invalid date format")

        return date.time
    }
}