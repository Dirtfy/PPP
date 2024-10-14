package com.dirtfy.ppp.ui.presenter.controller.common

import com.dirtfy.ppp.common.FlowState

interface Controller {

    fun <T, U> FlowState<T>.passMap(dataTransform: (data: T) -> U): FlowState<U> {//if success this transforms FlowState<T> to FlowState<U> (if fail-> no happen)
        return when(this) {
            is FlowState.Loading -> FlowState.loading()
            is FlowState.Failed -> {
                FlowState.failed(this.throwable)
            }
            is FlowState.Success -> {
                FlowState.success(dataTransform(this.data))
            }
        }
    }

    fun <T, U> FlowState<T>.ignoreMap(dataTransform: (data: T) -> U): U? {//they return the result of the dataTransform when they put FlowState<T>
        return when(this) {
            is FlowState.Loading, is FlowState.Failed -> { null }
            is FlowState.Success -> {
                dataTransform(this.data)
            }
        }
    }
}