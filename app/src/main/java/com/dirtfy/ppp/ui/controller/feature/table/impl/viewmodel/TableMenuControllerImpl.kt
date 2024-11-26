package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.UiTableMenuScreenState
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TableMenuControllerImpl @Inject constructor(
    private val menuBusinessLogic: MenuBusinessLogic
): TableMenuController, Tagger {

    private val menuListFlow = menuBusinessLogic.menuStream()
        .catch { cause ->
            Log.e(TAG, "menuList load failed")
            _screenData.update { it.copy(menuListState = UiScreenState(UiState.FAIL, cause)) }
        }
        .map {
            val menuList = it.map { data -> data.convertToUiMenu() }
            menuList
        }

    private val _screenData: MutableStateFlow<UiTableMenuScreenState>
        = MutableStateFlow(UiTableMenuScreenState())
    override val screenData: Flow<UiTableMenuScreenState>
        = _screenData
        .combine(menuListFlow) { state, menuList ->
            Log.d(TAG, "screenData combine 2")
            var newState = state.copy(
                menuList = menuList
            )
            if (state.menuList != menuList // 내용이 달라졌을 때
                || state.menuList !== menuList // 내용이 같지만 다른 인스턴스
                || menuList == emptyList<UiMenu>()) // emptyList()는 항상 같은 인스턴스
                newState = newState.copy(
                    menuListState = UiScreenState(state = UiState.COMPLETE)
                )
            newState
        }

    // TODO deprecate
    override suspend fun updateMenuList() {
    }

    override fun setMenuListState(state: UiScreenState) {
        _screenData.update { it.copy(menuListState = state) }
    }

}