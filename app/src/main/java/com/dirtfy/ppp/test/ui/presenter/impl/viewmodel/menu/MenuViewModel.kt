package com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.data.dto.DataMenu
import com.dirtfy.ppp.test.data.logic.MenuLogic
import com.dirtfy.ppp.test.ui.dto.menu.UiMenuState
import com.dirtfy.ppp.test.ui.presenter.contract.menu.MenuController
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.presenter.controller.common.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuLogic: MenuLogic
): ViewModel(), MenuController {

    private val menuList: Flow<List<UiMenu>>
    = menuLogic.menuStream().map {
        it.map { menu -> menu.convertToUiMenu() }
    }
    private val searchClue: MutableStateFlow<String>
    = MutableStateFlow("")
    private val newMenu: MutableStateFlow<UiMenu>
    = MutableStateFlow(UiMenu("", ""))

    override val uiState: StateFlow<UiMenuState>
    = menuList
        .map {
            it.filter { menu -> menu.name.contains(searchClue.value) }
        }
        .map {
            UiMenuState().copy(menuList = it)
        }
        .combine(searchClue) { state, searchClue ->
            state.copy(searchClue = searchClue)
        }
        .map {
            it.copy(
                menuList = it.menuList
                    .filter { menu -> menu.name.contains(it.searchClue) }
            )
        }
        .combine(newMenu) { state, newMenu ->
            state.copy(newMenu = newMenu)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiMenuState()
        )

    private fun DataMenu.convertToUiMenu(): UiMenu {
        return UiMenu(
            name = name,
            price = Utils.currencyFormatting(price),
        )
    }

    override fun updateSearchClue(clue: String) =
        searchClue.update { clue }

    override fun updateNewMenu(menu: UiMenu) =
        newMenu.update { menu }

    override fun createMenu(menu: UiMenu) = request {
        menuLogic.createMenu(menu.convertToDataMenu())
            .catch {

            }
            .collect {}
    }

    override fun deleteMenu(menu: UiMenu) = request {
        menuLogic.deleteMenu(menu.convertToDataMenu())
            .catch {

            }
            .collect {}
    }


    private fun request(job: suspend () -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}