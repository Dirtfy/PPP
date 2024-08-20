package com.dirtfy.ppp.common

sealed class FlowState<T> {
    class Loading<T> : FlowState<T>()
    data class Success<T>(val data: T) : FlowState<T>()
    data class Failed<T>(val message: String) : FlowState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}