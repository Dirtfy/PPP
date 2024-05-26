package com.dirtfy.ppp.model

interface Generator<InputDataType, OutputDataType> {

    fun generate(inputData: InputDataType): OutputDataType
}