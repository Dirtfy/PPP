package com.dirtfy.ppp.selling.tabling.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.selling.tabling.model.TableData
import com.dirtfy.ppp.selling.tabling.model.TableManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TableViewModel: ViewModel() {

    private val tablingManager = TableManager

    private val _tableList = MutableStateFlow(listOf<MutableStateFlow<TableOrderData>>())

    val tableList: StateFlow<List<StateFlow<TableOrderData>>>
        get() = _tableList

    init {
        viewModelScope.launch {
            (0..10).forEach {
                tablingManager.setupTable(it)

                val tableData = tablingManager.checkTable(it)

                _tableList.value += MutableStateFlow(tableData.convertToTableOrderData())
            }

//            _tableList.value = tablingManager.checkTables((0..10).toList())
//                .map { MutableStateFlow(it.convertToTableOrderData()) }
        }
    }

    private fun TableData.convertToTableOrderData(): TableOrderData {
        val countMap = HashMap<String, Int>()
        val priceMap = HashMap<String, Int>()

        menuNameList.indices.forEach {
            if (countMap[menuNameList[it]] == null) {
                countMap[menuNameList[it]] = 0
            }
            else {
                countMap[menuNameList[it]] = countMap[menuNameList[it]]!! + 1
            }

            priceMap[menuNameList[it]] = menuPriceList[it]
        }

        return TableOrderData(
            countMap = countMap,
            priceMap = priceMap
        )
    }
    private fun TableOrderData.convertToTableData(tableNumber: Int): TableData {
        val menuNameList = priceMap.keys.toList()
        val menuPriceList = menuNameList.map { priceMap[it]!! }

        return TableData(
            tableNumber,
            menuNameList,
            menuPriceList
        )
    }

    private fun <K> HashMap<K, Int>.count(key: K) {
        if (this[key] == null) {
            this[key] = 0
        }
        else {
            this[key] = this[key]!! + 1
        }
    }

    fun menuAdd(tableNumber: Int, name: String, price: Int) {
        viewModelScope.launch {
            val currentTableData = _tableList.value[tableNumber].value.convertToTableData(tableNumber)

            val updatedTableData = TableData(
                tableNumber,
                currentTableData.menuNameList+name,
                currentTableData.menuPriceList+price
            )

            tablingManager.updateMenu(updatedTableData)

            _tableList.value[tableNumber].value = updatedTableData.convertToTableOrderData()
        }
    }

}