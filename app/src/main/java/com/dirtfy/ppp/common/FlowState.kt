package com.dirtfy.ppp.common

sealed class FlowState<T> {
    class Loading<T> : FlowState<T>()
    data class Success<T>(var data: T) : FlowState<T>()
    data class Failed<T>(val throwable: Throwable) : FlowState<T>()

    fun set(newValue: T) {
        when(this) {
            is Success -> {
                data = newValue
            }
            else -> {}
        }
    }

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(throwable: Throwable) = Failed<T>(throwable)
    }
}