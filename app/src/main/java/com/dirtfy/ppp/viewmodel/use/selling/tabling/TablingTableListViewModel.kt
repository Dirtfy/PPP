package com.dirtfy.ppp.viewmodel.use.selling.tabling

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract.DTO.Table
import com.dirtfy.ppp.model.selling.tabling.TableManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class TablingTableListViewModel: TablingViewModelContract.TableList.API {

    private val tableModel: TableModelContract.API = TableManager

    private val _tableList: MutableState<List<Table>>
    = mutableStateOf(listOf())
    override val tableList: State<List<Table>>
        get() = _tableList

    private val _selectedTableNumber: MutableState<Int>
    = mutableIntStateOf(0)
    override val selectedTableNumber: State<Int>
        get() = _selectedTableNumber

    private val mergedSet: MutableList<MutableSet<Int>>
    = mutableListOf()

    override fun checkTables() {
        CoroutineScope(Dispatchers.Default).launch {
            val tableFormationList = listOf(
                11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 1
            )

            val currentTableList = mutableListOf<Table>()
            for (number in tableFormationList) {
                if (!tableModel.isSetup(number))
                    tableModel.setupTable(number)

                val newTable = if (number == 0) {
                    Table(
                        number = "0",
                        color = 0UL
                    )
                } else {
                    val checkedTable = tableModel.checkTable(number)

                    Table(
                        number = checkedTable.tableNumber.toString(),
                        color = Color.LightGray.value
                    )
                }

                currentTableList.add(newTable)
            }

            _tableList.value = currentTableList
        }
    }

    override fun selectTable(tableNumber: Int) {
        val nowList = _tableList.value.toMutableList()

        val numberSet = mergedSet.find {
            it.contains(tableNumber)
        }?: mutableSetOf(tableNumber)
        nowList.replaceAll {
            if (it.number.toInt() in numberSet) {
                val nowColor = Color(it.color)

                val newColor = Color(
                    red = nowColor.red,
                    blue = nowColor.blue,
                    green = nowColor.green,
                    alpha = 0.5f
                )

                it.copy(color = newColor.value)
            } else {
                it
            }
        }

        _tableList.value = nowList

        _selectedTableNumber.value = tableNumber
    }

    override fun deselectTable(tableNumber: Int) {
        val nowList = _tableList.value.toMutableList()

        val numberSet = mergedSet.find {
            it.contains(tableNumber)
        }?: mutableSetOf(tableNumber)
        nowList.replaceAll {
            if (it.number.toInt() in numberSet) {
                val nowColor = Color(it.color)

                val newColor = Color(
                    red = nowColor.red,
                    blue = nowColor.blue,
                    green = nowColor.green,
                    alpha = 1f
                )

                it.copy(color = newColor.value)
            } else {
                it
            }
        }

        _tableList.value = nowList

        _selectedTableNumber.value = 0
    }

    override fun mergeTable(baseTableNumber: Int, mergedTableNumber: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val baseTable = tableModel.checkTable(baseTableNumber)
            val mergedTable = tableModel.checkTable(mergedTableNumber)

            tableModel.mergeTable(baseTable, mergedTable)

            val nowList = _tableList.value.toMutableList()

            val newColor = Color(
                red = Random.nextInt(256),
                blue = Random.nextInt(256),
                green = Random.nextInt(256),
                alpha = 255
            )

            nowList.replaceAll {
                if (it.number.toInt() == baseTableNumber ||
                    it.number.toInt() == mergedTableNumber) {
                    it.copy(color = newColor.value)
                } else {
                    it
                }
            }

            val target = mergedSet.find {
                it.contains(baseTableNumber) || it.contains(mergedTableNumber)
            }
            if (target == null) {
                mergedSet.add(
                    mutableSetOf(baseTableNumber, mergedTableNumber)
                )
            } else {
                val base = mergedSet.find {
                    it.contains(baseTableNumber)
                }
                val merged = mergedSet.find {
                    it.contains(mergedTableNumber)
                }

                if (base == null) {
                    mergedSet.forEach {
                        if (it.contains(mergedTableNumber)) {
                            it.add(baseTableNumber)
                        }
                    }
                }
                else if(merged == null) {
                    mergedSet.forEach {
                        if (it.contains(baseTableNumber)) {
                            it.add(mergedTableNumber)
                        }
                    }
                }
                else {
                    mergedSet.removeIf {
                        it.contains(baseTableNumber) || it.contains(mergedTableNumber)
                    }
                    mergedSet.add(
                        (base + merged) as MutableSet<Int>
                    )
                }
            }

            _tableList.value = nowList
        }

    }

    override fun cleanTable(tableNumber: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            tableModel.cleanTable(tableNumber)

            val nowList = _tableList.value.toMutableList()

            val numberSet = mergedSet.find {
                it.contains(tableNumber)
            }?: mutableSetOf(tableNumber)
            nowList.replaceAll {
                if (it.number.toInt() in numberSet) {
                    it.copy(color = Color.LightGray.value)
                } else {
                    it
                }
            }

            _tableList.value = nowList
        }
    }
}