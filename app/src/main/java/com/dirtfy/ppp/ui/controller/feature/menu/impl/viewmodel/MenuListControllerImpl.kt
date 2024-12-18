package com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel

import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.feature.menu.MenuListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuListScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuListFlow = retryTrigger
        .flatMapLatest {
            menuBusinessLogic.menuStream()
                .map {
                    setMenuListState(UiScreenState(UiState.COMPLETE))
                    it
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
            var filteredList = menuList.filter {
                it.name.contains(state.searchClue)
            }
            if (state.searchAlcohol)
                filteredList = filteredList.filter {
                    it.category and(MenuCategory.ALCOHOL.code) != 0
                }
            if (state.searchLunch)
                filteredList = filteredList.filter {
                    it.category and(MenuCategory.LUNCH.code) != 0
                }
            if (state.searchDinner)
                filteredList = filteredList.filter {
                    it.category and(MenuCategory.DINNER.code) != 0
                }
            state.copy(
                menuList = filteredList.map { it.convertToUiMenu() }
                    .sortedBy { it.name }
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

    override fun updateSearchCategory(category: MenuCategory) {
        when (category){
            MenuCategory.ALCOHOL ->
                _screenData.update { it.copy(searchAlcohol = !it.searchAlcohol) }
            MenuCategory.LUNCH ->
                _screenData.update { it.copy(searchLunch = !it.searchLunch) }
            MenuCategory.DINNER ->
                _screenData.update { it.copy(searchDinner = !it.searchDinner) }
        }
    }

    override fun setMenuListState(state: UiScreenState) {
        _screenData.update { it.copy(menuListState = state) }
    }
}