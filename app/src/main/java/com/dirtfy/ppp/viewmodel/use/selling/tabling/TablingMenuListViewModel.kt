package com.dirtfy.ppp.viewmodel.use.selling.tabling

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.selling.MenuModelContract
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract.DTO.Menu
import com.dirtfy.ppp.model.selling.menu.managing.MenuRepository
import com.dirtfy.ppp.model.selling.tabling.TableManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TablingMenuListViewModel: TablingViewModelContract.MenuList.API, Tagger {

    private val tableModel: TableModelContract.API = TableManager
    private val menuModel: MenuModelContract.API = MenuRepository

    private val _rawMenuList: MutableList<MenuModelContract.DTO.Menu>
    = mutableListOf()
    private val _menuList: MutableState<List<Menu>>
    = mutableStateOf(listOf())
    override val menuList: State<List<Menu>>
        get() = _menuList

    private fun Menu.convertToModelMenu(): MenuModelContract.DTO.Menu {
        return MenuModelContract.DTO.Menu(
            null,
            this.name,
            this.price.toInt()
        )
    }
    private fun MenuModelContract.DTO.Menu.convertToViewModelMenu(): Menu {
        return Menu(
            this.name,
            this.price.toString()
        )
    }

    override fun checkMenu() {
        CoroutineScope(Dispatchers.Default).launch {
            _rawMenuList.clear()

            _rawMenuList.addAll(menuModel.read { true })

            _menuList.value = _rawMenuList.map { it.convertToViewModelMenu() }
        }
    }

    override fun orderMenu(menu: Menu, tableNumber: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val table = tableModel.checkTable(tableNumber)

            val newTable = table.copy(
                menuNameList = table.menuNameList + menu.name,
                menuPriceList = table.menuPriceList + menu.price.toInt()
            )

            tableModel.updateMenu(newTable)
        }
    }

    override fun cancelMenu(menu: Menu, tableNumber: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val table = tableModel.checkTable(tableNumber)

            val targetIndex = table.menuNameList.indexOf(menu.name)

            if (targetIndex < 0) {
                Log.e(TAG, "target is not found")
                return@launch
            }

            val newNameList = table.menuNameList.toMutableList()
            newNameList.removeAt(targetIndex)
            val newPriceList = table.menuPriceList.toMutableList()
            newPriceList.removeAt(targetIndex)

            val newTable = table.copy(
                menuNameList = newNameList,
                menuPriceList = newPriceList
            )

            tableModel.updateMenu(newTable)
        }
    }

}