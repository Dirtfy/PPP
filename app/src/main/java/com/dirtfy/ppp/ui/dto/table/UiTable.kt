package com.dirtfy.ppp.ui.dto.table

import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.data.dto.DataTable

data class UiTable(
    val number: String,
    val color: ULong
) {
    companion object {
        fun DataTable.convertToUiTable(): UiTable {
            return UiTable(
                number = number.toString(),
                color = Color.LightGray.value
            )
        }
    }
}
