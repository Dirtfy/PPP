package com.dirtfy.ppp.ui.presenter.viewmodel

import com.dirtfy.ppp.common.FlowState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface FlowStateBubble<T> {
    val backingProperty: MutableStateFlow<FlowState<T>>
    var value: T

    val property: StateFlow<FlowState<T>>
        get() = backingProperty

    suspend fun set(newValue: FlowState<T>) {
        when(newValue) {
            is FlowState.Success -> {
                value = newValue.data
            }
            else -> {}
        }
        backingProperty.value = newValue
    }

    fun get() = property
}