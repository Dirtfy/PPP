package com.dirtfy.ppp.common

import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

object Util {
    fun Timestamp.convertToCalendar(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = seconds * 1000L
        return cal
    }

    fun Calendar.convertToTimestamp(): Timestamp {
        return Timestamp(Date(timeInMillis))
    }

    fun Long.convertToTimestamp(): Timestamp {
        return Timestamp(Date(this))
    }

    fun Timestamp.convertToLong(): Long {
        return seconds * 1000L
    }
}