package com.dirtfy.ppp.test.ui.presenter.contract

import kotlinx.coroutines.flow.StateFlow

interface Controller<T> {

    val uiState: StateFlow<T>
}