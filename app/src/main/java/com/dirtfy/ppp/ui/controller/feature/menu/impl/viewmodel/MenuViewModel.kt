package com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToDataMenu
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.tagger.Tagger
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

class MenuViewModel: ViewModel(), MenuController, Tagger {

    private val menuService = MenuBusinessLogic(MenuFireStore())

    private val menuListFlow: Flow<List<UiMenu>> = menuService.menuStream().map {
        it.map { menu -> menu.convertToUiMenu() }
    }

    private val searchClueFlow = MutableStateFlow("")
    private val newMenuFlow = MutableStateFlow(UiMenu())
    private val newMenuStateFlow = MutableStateFlow(UiScreenState(UiState.COMPLETE))
    private val deleteMenuStateFlow = MutableStateFlow(UiScreenState(UiState.COMPLETE))

    override val screenData: StateFlow<UiMenuScreenState>
        = searchClueFlow
            .combine(newMenuFlow) { searchClue, newMenu ->
                UiMenuScreenState(
                    searchClue = searchClue,
                    newMenu = newMenu
                )
            }
            .combine(newMenuStateFlow) { state, newMenuState ->
                state.copy(
                    newMenuState = newMenuState
                )
            }
            .combine(deleteMenuStateFlow) { state, deleteMenuState ->
                state.copy(
                    deleteMenuState = deleteMenuState
                )
            }
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
            .catch { cause ->
                Log.e(TAG, "uiMenuScreenState - combine failed \n ${cause.message}")

                // TODO 더 기가 막힌 방법 생각해보기
                UiMenuScreenState(
                    searchClue = searchClueFlow.value,
                    newMenu = newMenuFlow.value,
                    menuListState = UiScreenState(UiState.FAIL, cause.message)
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
        searchClueFlow.update { clue }
    }

    override fun updateNewMenu(menu: UiMenu) {
        newMenuFlow.update { menu }
    }

    override suspend fun createMenu(menu: UiMenu) {
        newMenuStateFlow.update { UiScreenState(UiState.LOADING) }
        if (menu.name == "") {
            newMenuStateFlow.update { UiScreenState(UiState.FAIL, MenuException.BlankName().message) }
            return
        }
        if (menu.price == "") {
            newMenuStateFlow.update { UiScreenState(UiState.FAIL, MenuException.BlankPrice().message) }
            return
        }

        menuService.createMenu(
            menu.convertToDataMenu()
        ).catch { cause ->
            Log.e(TAG, "createMenu() - createMenu failed")
            newMenuStateFlow.update { UiScreenState(UiState.FAIL, cause.message) }
        }.collect {
            newMenuFlow.update { UiMenu() }
            newMenuStateFlow.update { UiScreenState(UiState.COMPLETE) }
        }
    }

    override suspend fun deleteMenu(menu: UiMenu) {
        deleteMenuStateFlow.update { UiScreenState(UiState.LOADING) }

        menuService.deleteMenu(
            menu.convertToDataMenu()
        ).catch { cause ->
            Log.e(TAG, "deleteMenu() - deleteMenu failed")
            deleteMenuStateFlow.update { UiScreenState(UiState.FAIL, cause.message) }
        }.collect {
            deleteMenuStateFlow.update { UiScreenState(UiState.COMPLETE) }
        }
    }

    override fun request(job: suspend MenuController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}