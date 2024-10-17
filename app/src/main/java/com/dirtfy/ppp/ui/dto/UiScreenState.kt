package com.dirtfy.ppp.ui.dto

data class UiScreenState(
    val state: UiState = UiState.LOADING,
    val failMessage: String? = null
)

enum class UiState {
    LOADING, COMPLETE, FAIL
}
