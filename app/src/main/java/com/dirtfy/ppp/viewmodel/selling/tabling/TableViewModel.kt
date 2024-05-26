package com.dirtfy.ppp.viewmodel.selling.tabling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.model.selling.tabling.TableData
import com.dirtfy.ppp.model.selling.tabling.TableManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TableViewModel: ViewModel() {

    private val tablingManager = TableManager

    private val _tableList = MutableStateFlow(listOf<MutableStateFlow<TableOrderData>>())
    val tableList: StateFlow<List<TableOrderData>>
        get() = MutableStateFlow(_tableList.value.map { it.value })

    private val _selectedTableNumber = MutableStateFlow(0)
    val selectedTable: StateFlow<TableOrderData>
        get() = _tableList.value[_selectedTableNumber.value]

    init {
        viewModelScope.launch {
            (0..10).forEach {
                if (!tablingManager.isSetup(it))
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
                countMap[menuNameList[it]] = 1
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

    fun groupTable(tableNumberList: List<Int>) {
        viewModelScope.launch {
            (1..<tableNumberList.size).forEach {
                tablingManager.mergeTable(
                    _tableList.value[0].value.convertToTableData(tableNumberList[0]),
                    _tableList.value[it].value.convertToTableData(tableNumberList[it])
                )
            }
        }
    }

    fun cleanTable(tableNumber: Int) {
        viewModelScope.launch {
            tablingManager.cleanTable(tableNumber)
        }
    }

}