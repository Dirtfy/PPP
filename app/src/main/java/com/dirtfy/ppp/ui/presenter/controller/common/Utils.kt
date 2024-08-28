package com.dirtfy.ppp.ui.presenter.controller.common

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun currencyFormatting(price: Int): String {
        return DecimalFormat("#,###").format(price)
    }

    fun currencyReformatting(price: String): Int {
        return price.split(",").joinToString(separator = "").toInt()
    }

    fun timestampFormatting_YMD(time: Long): String {
        val dateFormat = "yyyy.MM.dd"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.format(date)
    }

    fun timestampReformatting_YMD(time: String): Long {
        val dateFormat = "yyyy.MM.dd"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.parse(time)?.time ?: -1L
    }

    fun timestampFormatting_YMDHm(time: Long): String {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.format(date)
    }

    fun timestampReformatting_YMDHm(time: String): Long {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        return simpleDateFormat.parse(time)?.time ?: -1L
    }

    fun timestampFormatting_YMDHmms(time: Long): String {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)

        val formed = simpleDateFormat.let {
            it.parse(it.format(date))?.time!!
        }
        val sec = time - formed

        return simpleDateFormat.format(date) + sec.toString()
    }

    fun timestampReformatting_YMDHmms(time: String): Long {
        val dateFormat = "yyyy.MM.dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.KOREA)
        val underMinute = time.substring(16).toLong()

        return simpleDateFormat.parse(time.substring(0, 16))?.time?.plus(underMinute) ?: -1L
    }
}