package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.dto.feature.menu.MenuCategory
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMenuScreenState
import com.dirtfy.tagger.Tagger
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

class TableMenuControllerImpl @Inject constructor(
    private val menuBusinessLogic: MenuBusinessLogic
): TableMenuController, Tagger {

    private val retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuListFlow = retryTrigger
        .flatMapLatest {
            menuBusinessLogic.menuStream().map {
                _screenData.update { before -> before.copy(menuListState = UiScreenState(UiState.COMPLETE)) }
                it
            }.onStart {
                _screenData.update { before -> before.copy(menuListState = UiScreenState(UiState.LOADING)) }
                emit(emptyList())
            }.catch { cause ->
                Log.e(TAG, "menuList load failed")
                _screenData.update { it.copy(menuListState = UiScreenState(UiState.FAIL, cause)) }
                emit(emptyList())
            }

        }

    private val _screenData: MutableStateFlow<UiTableMenuScreenState>
        = MutableStateFlow(UiTableMenuScreenState())
    override val screenData: Flow<UiTableMenuScreenState>
        = _screenData
        .combine(menuListFlow) { state, menuList ->
            val filteredList = menuList.filter { it.category and(state.nowMenuCategory.code) != 0 }
                .map { it.convertToUiMenu() }
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

    override fun setNowMenuCategory(category: MenuCategory) {
        _screenData.update { it.copy(nowMenuCategory = category) }
    }

    override fun setMenuListState(state: UiScreenState) {
        _screenData.update { it.copy(menuListState = state) }
    }

}