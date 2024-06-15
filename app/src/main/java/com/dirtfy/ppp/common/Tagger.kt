package com.dirtfy.ppp.common

interface Tagger {

    val TAG: String
        get() {
            return this::class.simpleName +
                    Thread.currentThread().stackTrace[4].methodName
        }

}