package com.dirtfy.ppp.ui.presenter.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Bubble<T> {
    val backingProperty: MutableStateFlow<T>
    val property: StateFlow<T>
        get() = backingProperty
    val value: T
        get() = backingProperty.value

    fun set(newValue: T) {
        backingProperty.value = newValue
    }
    fun get() = property
}