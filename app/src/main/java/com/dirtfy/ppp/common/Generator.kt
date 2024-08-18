package com.dirtfy.ppp.common

interface Generator<I, O> {
    fun generate(inputData: I): O
}