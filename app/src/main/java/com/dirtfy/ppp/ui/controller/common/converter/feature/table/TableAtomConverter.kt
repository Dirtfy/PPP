package com.dirtfy.ppp.ui.controller.common.converter.feature.table

import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder

object TableAtomConverter {

    fun DataTableOrder.convertToUiTableOrder(): UiTableOrder {
        return UiTableOrder(
            name = name,
            price = StringFormatConverter.formatCurrency(price),
            count = count.toString()
        )
    }

    fun DataTable.convertToUiTable(): UiTable {
        return UiTable(
            number = number.toString(),
            color = Color.LightGray.value
        )
    }




    fun UiTableOrder.convertToDataTableOrder(): DataTableOrder {
        return DataTableOrder(
            name = name,
            price = StringFormatConverter.parseCurrency(price),
            count = count.toInt()
        )
    }


}