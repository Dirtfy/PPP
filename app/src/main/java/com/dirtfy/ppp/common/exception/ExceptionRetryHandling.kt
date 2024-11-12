package com.dirtfy.ppp.common.exception

object ExceptionRetryHandling {
    fun isTableRetry(message: String?): Boolean {
        return when (message) {
            TableException.GroupLoss().message,
            TableException.MemberLoss().message,
            TableException.NonEnoughMenuToCancel().message -> true
            else -> false  // 그 외의 에러는 재시도 불가능
        }
    }
}
