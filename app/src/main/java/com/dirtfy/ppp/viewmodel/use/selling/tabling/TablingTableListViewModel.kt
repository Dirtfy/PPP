package com.dirtfy.ppp.viewmodel.use.selling.tabling

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract.DTO.Table
import com.dirtfy.ppp.model.selling.tabling.TableManager
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class TablingTableListViewModel: TablingViewModelContract.TableList.API, Tagger {

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

    private fun getRandomColor(): ULong {
        var newColor = Color(
            red = Random.nextInt(256),
            blue = Random.nextInt(256),
            green = Random.nextInt(256),
            alpha = 255
        )

        while(newColor.run { red + green + blue } < 2) {
            newColor = Color(
                red = Random.nextInt(256),
                blue = Random.nextInt(256),
                green = Random.nextInt(256),
                alpha = 255
            )
        }

        return newColor.value
    }

    override fun checkTables() {
        CoroutineScope(Dispatchers.Default).launch {
            val tableFormationList = listOf(
                11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
                0,  0, 0, 0, 0, 0, 0, 0, 0, 1
            )

            mergedSet.clear()

            val currentTableList = mutableListOf<Table>()

            (0..11).let {
                val resultList = tableModel.isSetup(it.toList())

                resultList.zip(it).forEach { pair ->
                    if (!pair.first)
                        tableModel.setupTable(pair.second)
                }

                Log.d(TAG, "table $it is set")
            }

            Log.d(TAG, "start checking")

            val checkedTableList = tableModel.checkTables(tableFormationList)
            checkedTableList.zip(tableFormationList).forEach { pair ->
                val checkedTable = pair.first
                val actualNumber = checkedTable.tableNumber
                val number = pair.second

                if (actualNumber != number) {
                    val set = mergedSet.find { it.contains(actualNumber) }
                    if (set == null)
                        mergedSet.add(mutableSetOf(actualNumber, number))
                    else
                        set.add(number)
                }

                currentTableList.add(
                    when(number) {
                        0 -> Table(number = "0", color = 0UL)
                        else -> {
                            Table(
                                number = number.toString(),
                                color = Color.LightGray.value
                            )
                        }
                    }
                )
            }

            Log.d(TAG, "color group")

            mergedSet.forEach { set ->
                val newColor = getRandomColor()

                set.forEach {
                    currentTableList.replaceAll { table ->
                        if (table.number.toInt() == it)
                            table.copy(color = newColor)
                        else
                            table
                    }
                }
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
                    green = nowColor.green,
                    blue = nowColor.blue,
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
                    green = nowColor.green,
                    blue = nowColor.blue,
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

            val target = mergedSet.find {
                it.contains(baseTableNumber) || it.contains(mergedTableNumber)
            }
            val set = mutableSetOf(baseTableNumber, mergedTableNumber)
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
                            set.addAll(it)
                        }
                    }
                }
                else if(merged == null) {
                    mergedSet.forEach {
                        if (it.contains(baseTableNumber)) {
                            it.add(mergedTableNumber)
                            set.addAll(it)
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
                    set.addAll(base + merged)
                }
            }

            val newColor = getRandomColor()
            nowList.replaceAll {
                if (it.number.toInt() in set) {
                    it.copy(color = newColor)
                } else {
                    it
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

            mergedSet.removeIf { it.contains(tableNumber) }

            _tableList.value = nowList
        }
    }
}