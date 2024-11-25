package com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.controller.feature.menu.MenuListController
import com.dirtfy.ppp.ui.controller.feature.menu.MenuUpdateController
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.tagger.Tagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val listController: MenuListController,
    private val updateController: MenuUpdateController
): ViewModel(), MenuController, Tagger {

    override val screenData: StateFlow<UiMenuScreenState>
        = listController.screenData
        .combine(updateController.screenData) { listScreenData, updateScreenData ->
            UiMenuScreenState(
                menuList = listScreenData.menuList,
                searchClue = listScreenData.searchClue,
                newMenu = updateScreenData.newMenu,
                menuListState = listScreenData.menuListState,
                addMenuState = updateScreenData.addMenuState,
                deleteMenuState = updateScreenData.deleteMenuState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiMenuScreenState()
        )

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateMenuList() {
    }

    override fun updateSearchClue(clue: String) {
        listController.updateSearchClue(clue)
    }

    override fun updateNewMenu(menu: UiMenu) {
        updateController.updateNewMenu(menu)
    }

    override suspend fun createMenu() {
        updateController.createMenu()
    }

    override suspend fun deleteMenu(menu: UiMenu) {
        updateController.deleteMenu(menu)
    }

    override fun request(job: suspend MenuController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}