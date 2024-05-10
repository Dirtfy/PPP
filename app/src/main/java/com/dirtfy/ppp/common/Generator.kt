package com.dirtfy.ppp.common

interface Generator<InputDataType, OutputDataType> {

    fun generate(inputData: InputDataType): OutputDataType
}