package com.dirtfy.ppp.common.exception

sealed class RecordException(
    massage: String
): CustomException(massage) {

    class IssuedNameLoss: RecordException("issued name is not found")
    class DifferenceLoss: RecordException("difference is not found")
    class TimestampLoss: RecordException("time stamp is not found")
}