package com.dirtfy.ppp.ui.state.common

data class UiScreenState(
    val state: UiState = UiState.LOADING,
    val failMessage: String? = null
)

