package com.dirtfy.ppp.ui.holder

import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import kotlinx.coroutines.launch

interface DataHolder {

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
}