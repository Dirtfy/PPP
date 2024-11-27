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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MenuListControllerImpl @Inject constructor(
    private val menuBusinessLogic: MenuBusinessLogic
): MenuListController {

    private val retryTrigger = MutableStateFlow(0)

    private val _screenData: MutableStateFlow<UiMenuListScreenState>
        = MutableStateFlow(UiMenuListScreenState())

    private val menuListFlow: Flow<List<UiMenu>> = retryTrigger
        .flatMapLatest {
            menuBusinessLogic.menuStream()
                .map {
                    setMenuListState(UiScreenState(UiState.COMPLETE))
                    it.map { menu -> menu.convertToUiMenu() }
                }
                .onStart {
                    setMenuListState(UiScreenState(UiState.LOADING))
                    emit(emptyList())
                }
                .catch { cause ->
                    setMenuListState(UiScreenState(UiState.FAIL, cause))
                    emit(emptyList())
                }
        }

    override val screenData: Flow<UiMenuListScreenState>
         = _screenData
        .combine(menuListFlow) { state, menuList ->
            val filteredList = menuList.filter {
                it.name.contains(state.searchClue)
            }
            state.copy(
                menuList = filteredList
            )
        }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateMenuList() {
    }

    override fun retryUpdateMenuList() {
        retryTrigger.value += 1
    }

    override fun updateSearchClue(clue: String) {
        _screenData.update { it.copy(searchClue = clue) }
    }

    override fun setMenuListState(state: UiScreenState) {
        _screenData.update { it.copy(menuListState = state) }
    }
}