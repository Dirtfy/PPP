package com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel

import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.feature.menu.MenuListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuListScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MenuListControllerImpl @Inject constructor(
    private val menuBusinessLogic: MenuBusinessLogic
): MenuListController {

    private val _screenData: MutableStateFlow<UiMenuListScreenState>
        = MutableStateFlow(UiMenuListScreenState())

    private val menuListFlow: Flow<List<UiMenu>> = menuBusinessLogic.menuStream()
        .map { it.map { menu -> menu.convertToUiMenu() } }
        .catch { cause ->
            _screenData.update { it.copy(menuListState = UiScreenState(UiState.FAIL, cause.message)) }
        }

    override val screenData: Flow<UiMenuListScreenState>
         = _screenData
        .combine(menuListFlow) { state, menuList ->
            val filteredList = menuList.filter {
                it.name.contains(state.searchClue)
            }

            var newState = state.copy(
                menuList = filteredList
            )

            if (state.menuList != menuList /* 내용이 달라졌을 때 */
                || state.menuList !== menuList /* 내용이 같지만 다른 인스턴스 */
                || menuList == emptyList<UiMenu>() /* emptyList()는 항상 같은 인스턴스 */)
                newState = newState.copy(
                    menuListState = UiScreenState(UiState.COMPLETE)
                )

            newState
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateMenuList() {
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }
}