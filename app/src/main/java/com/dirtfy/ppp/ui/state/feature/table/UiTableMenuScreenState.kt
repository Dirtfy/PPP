package com.dirtfy.ppp.ui.state.feature.table

import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu

data class UiTableMenuScreenState(
    val menuList: List<UiMenu> = emptyList(),
    val nowMenuCategory: MenuCategory = MenuCategory.ALCOHOL,

    val menuListState: UiScreenState = UiScreenState(),
)