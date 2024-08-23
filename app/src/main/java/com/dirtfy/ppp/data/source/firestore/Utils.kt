package com.dirtfy.ppp.data.source.firestore

import com.google.firebase.Timestamp

object Utils {

    fun Timestamp.convertToMilliseconds(): Long {
        return seconds*1000L + nanoseconds/(1000L*1000L)
    }
}