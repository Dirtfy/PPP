package com.dirtfy.ppp.test.ui.presenter.contract.menu

import com.dirtfy.ppp.test.ui.dto.menu.UiMenuState
import com.dirtfy.ppp.test.ui.presenter.contract.Controller
import com.dirtfy.ppp.ui.dto.UiMenu

interface MenuController: Controller<UiMenuState> {

    fun updateSearchClue(clue: String)
    fun updateNewMenu(menu: UiMenu)

    fun createMenu(menu: UiMenu)

    fun deleteMenu(menu: UiMenu)
}