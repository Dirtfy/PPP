package com.dirtfy.ppp.viewmodel.use.selling

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.model.selling.menu.managing.MenuData
import com.dirtfy.ppp.model.selling.menu.managing.MenuRepository
import com.dirtfy.ppp.model.selling.tabling.TableData
import com.dirtfy.ppp.model.selling.tabling.TableManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TablingViewModel: ViewModel(), TablingContract.API {

    private val tablingManager = TableManager
    private val menuRepository = MenuRepository

    private val _tableDataList: MutableStateFlow<List<TableData>>
    = MutableStateFlow(listOf())
    private val _tableColorList: MutableList<ULong>
    = MutableList(11) { Color.LightGray.value }
    override val tableList: StateFlow<List<TablingContract.DTO.Table>>
        get() = MutableStateFlow(
            _tableDataList.value.map {
                TablingContract.DTO.Table(
                    "${it.tableNumber+1}",
                    _tableColorList[it.tableNumber]
                )
            }
        )

    private val _menuList: MutableList<MenuData>
    = mutableListOf()
    override val menuList: StateFlow<List<TablingContract.DTO.Menu>>
        get() = MutableStateFlow(
            _menuList.map {
                TablingContract.DTO.Menu(
                    it.name,
                    it.price.toString()
                )
            }
        )

    private var _selectedTableNumber = 0
    override val orderList: StateFlow<List<TablingContract.DTO.Order>>
        get() = MutableStateFlow(
            if (_selectedTableNumber == 0) listOf()
            else {
                val menuCountMap = hashMapOf<String, Int>()
                val menuPriceMap = hashMapOf<String, Int>()

                val targetTable = _tableDataList.value
                    .find { it.tableNumber == _selectedTableNumber }!!
                val namePriceList = targetTable.menuNameList
                    .zip(targetTable.menuPriceList)

                namePriceList.forEach {
                    val name = it.first
                    val currentValue = menuCountMap[name]?:0
                    menuCountMap[name] = currentValue + 1

                    val price = it.second
                    menuPriceMap[name] = price
                }

                val menuKeys = targetTable.menuNameList.toSet().toList()

                menuKeys.map {
                    TablingContract.DTO.Order(
                        name = it,
                        price = menuPriceMap[it].toString(),
                        count = menuCountMap[it].toString()
                    )
                }
            }
        )
    override val total: StateFlow<TablingContract.DTO.Total>
        get() = MutableStateFlow(
            TablingContract.DTO.Total(
                orderList.value
                    .sumOf { it.price.toInt() * it.count.toInt() }
                    .toString()
            )
        )

    override fun checkOrder() {
        viewModelScope.launch {
            val targetIndex = _tableDataList.value
                .indexOfFirst { it.tableNumber == _selectedTableNumber }

            if (targetIndex == -1) return@launch

            val newValue = _tableDataList.value.toMutableList()
            newValue[targetIndex] =
                tablingManager.checkTable(_selectedTableNumber)

            _tableDataList.value = newValue
        }
    }

    override fun checkTable() {
        viewModelScope.launch {
            _tableDataList.value = (1..11)
                .map { tablingManager.checkTable(it) }
        }
    }

    override fun checkMenu() {
        viewModelScope.launch {
            _menuList.clear()
            _menuList.addAll(menuRepository.read { true })
        }
    }

    override fun selectTable() {
        TODO("Not yet implemented")
    }

    override fun deselectTable() {
        TODO("Not yet implemented")
    }

    override fun mergeTable() {
        TODO("Not yet implemented")
    }

    override fun cleanTable() {
        TODO("Not yet implemented")
    }

    override fun payTable() {
        TODO("Not yet implemented")
    }

    override fun orderMenu() {
        TODO("Not yet implemented")
    }

    override fun orderInstantMenu() {
        TODO("Not yet implemented")
    }

    override fun cancelMenu() {
        TODO("Not yet implemented")
    }
}