package com.dirtfy.ppp.ui.controller.common.converter.feature.menu

import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu

object MenuAtomConverter {

    fun DataMenu.convertToUiMenu(): UiMenu {
        return UiMenu(
            name = name,
            price = StringFormatConverter.formatCurrency(price),
        )
    }

    fun UiMenu.convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name,
            price = StringFormatConverter.parseCurrency(price),
        )
    }

}