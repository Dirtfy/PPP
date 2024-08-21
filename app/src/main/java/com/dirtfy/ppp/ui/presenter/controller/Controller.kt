package com.dirtfy.ppp.ui.presenter.controller

import com.dirtfy.ppp.common.FlowState

interface Controller {

    fun <T, U> FlowState<T>.passMap(dataTransform: (data: T) -> U): FlowState<U> {
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

    fun <T, U> FlowState<T>.ignoreMap(dataTransform: (data: T) -> U): U? {
        return when(this) {
            is FlowState.Loading, is FlowState.Failed -> { null }
            is FlowState.Success -> {
                dataTransform(this.data)
            }
        }
    }
}