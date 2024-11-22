package com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel

import android.util.Log
import com.dirtfy.ppp.common.exception.MenuException
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToDataMenu
import com.dirtfy.ppp.ui.controller.feature.menu.MenuUpdateController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.UiMenuUpdateScreenState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MenuUpdateControllerImpl @Inject constructor(
    private val menuBusinessLogic: MenuBusinessLogic
): MenuUpdateController, Tagger {

    private val _screenData: MutableStateFlow<UiMenuUpdateScreenState>
        = MutableStateFlow(UiMenuUpdateScreenState())

    override val screenData: Flow<UiMenuUpdateScreenState>
        get() = _screenData

    override fun updateNewMenu(menu: UiMenu) {
        _screenData.update { it.copy(newMenu = menu) }
    }

    override suspend fun createMenu() {
        _screenData.update { it.copy(addMenuState = UiScreenState(UiState.LOADING)) }

        val menu = _screenData.value.newMenu

        if (menu.name == "") {
            _screenData.update { it.copy(
                addMenuState = UiScreenState(UiState.FAIL, MenuException.BlankName().message)
            ) }
            return
        }
        if (menu.price == "") {
            _screenData.update { it.copy(
                addMenuState = UiScreenState(UiState.FAIL, MenuException.BlankPrice().message)
            ) }
            return
        }

        menuBusinessLogic.createMenu(
            menu.convertToDataMenu()
        ).catch { cause ->
            Log.e(TAG, "createMenu() - createMenu failed")
            _screenData.update { it.copy(
                addMenuState = UiScreenState(UiState.FAIL, cause.message)
            ) }
        }.collect {
            _screenData.update { it.copy(
                newMenu = UiMenu(),
                addMenuState = UiScreenState(UiState.COMPLETE)
            ) }
        }
    }

    override suspend fun deleteMenu(menu: UiMenu) {
        _screenData.update { it.copy(
            deleteMenuState = UiScreenState(UiState.LOADING)
        ) }

        menuBusinessLogic.deleteMenu(
            menu.convertToDataMenu()
        ).catch { cause ->
            Log.e(TAG, "deleteMenu() - deleteMenu failed")
            _screenData.update { it.copy(
                deleteMenuState = UiScreenState(UiState.FAIL, cause.message)
            ) }
        }.collect {
            _screenData.update { it.copy(
                deleteMenuState = UiScreenState(UiState.COMPLETE)
            ) }
        }
    }
}