package com.dirtfy.ppp.ui.controller.common.converter.feature.menu

import com.dirtfy.ppp.data.dto.feature.menu.DataMenu
import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiNewMenu

object MenuAtomConverter {

    fun DataMenu.convertToUiMenu(): UiMenu {
        return UiMenu(
            name = name,
            price = StringFormatConverter.formatCurrency(price),
            category = menuCategoryIntToString(category)
        )
    }

    fun UiMenu.convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name,
            price = StringFormatConverter.parseCurrency(price),
            category = menuCategoryStringToInt(category)
        )
    }

    fun UiNewMenu.convertToDataMenu(): DataMenu {
        return DataMenu(
            name = name,
            price = StringFormatConverter.parseCurrency(price),
            category = menuCategoryBooleanToInt(isAlcohol, isLunch, isDinner)
        )
    }

    private fun menuCategoryIntToString(category: Int): String {
        var result = ""
        MenuCategory.entries
            .filter { (it.code and(category)) != 0 }
            .forEach {
                if (result.isNotEmpty()) result += "|"
                result += it.koName
            }
        return result
    }

    private fun menuCategoryStringToInt(category: String): Int {
        var result = 0
        MenuCategory.entries
            .filter { category.contains(it.koName) }
            .forEach {
                result = result or(it.code)
            }
        return result
    }

    private fun menuCategoryBooleanToInt(
        isAlcohol: Boolean,
        isLunch: Boolean,
        isDinner: Boolean
    ): Int {
        var result = 0
        if (isAlcohol) result = result or(MenuCategory.ALCOHOL.code)
        if (isLunch) result = result or(MenuCategory.LUNCH.code)
        if (isDinner) result = result or(MenuCategory.DINNER.code)
        return result
    }

}