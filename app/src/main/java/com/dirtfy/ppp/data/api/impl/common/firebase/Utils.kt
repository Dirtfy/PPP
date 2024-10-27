package com.dirtfy.ppp.data.api.impl.common.firebase

import com.google.firebase.Timestamp

object Utils {

    fun Timestamp.convertToMilliseconds(): Long {
        return seconds*1000L + nanoseconds/(1000L*1000L)
    }
}