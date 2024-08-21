package com.dirtfy.ppp.ui.holder.menu

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.holder.DataHolder
import com.dirtfy.ppp.ui.holder.MenuViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MenuHolder: DataHolder {
    val menuList: StateFlow<FlowState<List<HolderMenu>>>

    val newMenu: StateFlow<HolderMenu>

    suspend fun updateMenuList()

    suspend fun updateNewMenu(menu: HolderMenu)

    suspend fun createMenu(menu: HolderMenu)

    suspend fun deleteMenu(menu: HolderMenu)

    fun request(job: suspend MenuHolder.() -> Unit)
}