package com.dirtfy.ppp.ui.presenter.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.logic.MenuService
import com.dirtfy.ppp.data.logic.TableService
import com.dirtfy.ppp.data.source.firestore.menu.MenuFireStore
import com.dirtfy.ppp.data.source.firestore.record.RecordFireStore
import com.dirtfy.ppp.data.source.firestore.table.TableFireStore
import com.dirtfy.ppp.ui.dto.UiMenu
import com.dirtfy.ppp.ui.dto.UiMenu.Companion.convertToUiMenu
import com.dirtfy.ppp.ui.dto.UiPointUse
import com.dirtfy.ppp.ui.dto.UiTable
import com.dirtfy.ppp.ui.dto.UiTableOrder
import com.dirtfy.ppp.ui.presenter.controller.TableController
import com.dirtfy.ppp.ui.presenter.controller.common.Utils
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import kotlin.random.Random

class TableViewModel: ViewModel(), TableController, Tagger {

    private val tableService: TableService = TableService(TableFireStore(), RecordFireStore())
    private val menuService: MenuService = MenuService(MenuFireStore())

    private val groupColorSet = mutableSetOf<ULong>()
    private val defaultColor = Color.LightGray.value

    private val tableFormation: List<Int>
    = listOf(
        11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
         0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
         0,  0, 0, 0, 0, 0, 0, 0, 0, 1
    )
    private val selectedTableSet: MutableSet<Int>
    = mutableSetOf()
    private val selectedTableNumber
        get() = selectedTableSet.toList()[0]
    private val groupMap: MutableList<Int>
    = MutableList(12) { it }

    private val _tableList: MutableStateFlow<FlowState<List<UiTable>>>
    = MutableStateFlow(FlowState.loading())
    override val tableList: StateFlow<FlowState<List<UiTable>>>
        get() = _tableList

    private val _orderList: MutableStateFlow<FlowState<List<UiTableOrder>>>
    = MutableStateFlow(FlowState.loading())
    override val orderList: StateFlow<FlowState<List<UiTableOrder>>>
        get() = _orderList

    private val _menuList: MutableStateFlow<FlowState<List<UiMenu>>>
    = MutableStateFlow(FlowState.loading())
    override val menuList: StateFlow<FlowState<List<UiMenu>>>
        get() = _menuList

    private val _orderTotalPrice: MutableStateFlow<String>
    = MutableStateFlow("")
    override val orderTotalPrice: StateFlow<String>
        get() = _orderTotalPrice

    private val _pointUse: MutableStateFlow<UiPointUse>
    = MutableStateFlow(UiPointUse("",""))
    override val pointUse: StateFlow<UiPointUse>
        get() = _pointUse

    private fun DataTable.convertToUiTable(): UiTable {
        return UiTable(
            number = number.toString(),
            color = defaultColor
        )
    }

    private fun DataTableOrder.convertToUiTableOrder(): UiTableOrder {
        return UiTableOrder(
            name = name,
            price = Utils.currencyFormatting(price),
            count = count.toString()
        )
    }

    private suspend fun _updateTableList() {
        tableService.readTables().conflate().collect {
            _tableList.value = it.passMap { data ->
                val newList = data.map { table -> table.convertToUiTable() }.toMutableList()
                Log.d(TAG, "$newList")

                data.forEach { table ->
                    groupMap[table.number] = table.group
                }

                // TODO group color 배정 다시 생각
//                groupMap.toSet().toList().forEach { group ->
//                    var groupColor = getRandomColor()
//                    while (groupColorSet.contains(groupColor)) {
//                        groupColor = getRandomColor()
//                    }
//                    groupColorSet.add(groupColor)
//
//                    data.filter { table ->
//                        table.number != table.group ||
//                                groupMap[table.number] == group
//                    }
//
//                    (1..11).filter { tableNumber ->
//                        groupMap[tableNumber] == group
//                    }.let { member ->
//                        newList.replaceAll { table ->
//                            if (member.contains(table.number.toInt()))
//                                table.copy(color = groupColor)
//                            else table
//                        }
//                    }
//                }

                tableFormation.map { tableNumber ->
                    if (tableNumber == 0)
                        UiTable("0", Color.Transparent.value)
                    else {
                        Log.d(TAG, "$tableNumber")
                        newList.find { table ->
                            table.number == tableNumber.toString()
                        }?: throw TableException.NumberLoss()
                    }
                }
            }
        }
    }
    override fun updateTableList() = request {
        _updateTableList()
    }

    private suspend fun _updateOrderList(tableNumber: Int) {
        tableService.readOrders(tableNumber)
            .conflate().collect {
                _orderList.value = it.passMap { data ->
                    data.map { order -> order.convertToUiTableOrder() }
                }
            }
    }
    override fun updateOrderList(table: UiTable) = request {
        val tableNumber = table.number.toInt()
        _updateOrderList(tableNumber)
    }

    private suspend fun _updateMenuList() {
        menuService.readMenu().conflate().collect {
            _menuList.value = it.passMap { data ->
                data.map { menu -> menu.convertToUiMenu() }
            }
        }
    }
    override fun updateMenuList() = request {
        _updateMenuList()
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        _pointUse.value = pointUse
    }

    override fun clickTable(table: UiTable) {
        val tableNumber = table.number.toInt()
        val group = groupMap[tableNumber]
        val member = mutableListOf<Int>()
        groupMap.indices.forEach {
            if (groupMap[it] == group)
                member.add(it)
        }

        val newColor = if (selectedTableSet.contains(tableNumber)) {
            selectedTableSet.removeAll(member.toSet())
            Color(table.color).copy(alpha = 1f)
        } else {
            selectedTableSet.addAll(member.toSet())
            Color(table.color).copy(alpha = 0.5f)
        }

        _tableList.value = _tableList.value.passMap {
            val newList = it.toMutableList()

            newList.map { nowTable ->
                if (member.contains(nowTable.number.toInt())) {
                    nowTable.copy(color = newColor.value)
                } else {
                    nowTable
                }
            }
        }
    }

    private fun getRandomColor(): ULong {
        return Color(
            red = Random.nextInt(150, 256),
            green = Random.nextInt(150, 256),
            blue = Random.nextInt(150, 256),
            alpha = 255
        ).value
    }

    private suspend fun _mergeTable(tableList: List<Int>) {
        tableService.mergeTables(tableList)
            .conflate().collect {
                tableService.getGroup(tableList[0]).conflate().collect { group ->
                    it.passMap {
                        group.passMap { groupNumber ->
                            _tableList.value = _tableList.value.passMap { data ->
                                var groupColor = getRandomColor()
                                while (groupColorSet.contains(groupColor)) {
                                    groupColor = getRandomColor()
                                }
                                groupColorSet.add(groupColor)

                                data.map { table ->
                                    if (selectedTableSet.contains(table.number.toInt())){
                                        groupMap[table.number.toInt()] = groupNumber
                                        table.copy(color = groupColor)
                                    }
                                    else
                                        table
                                }
                            }
                        }
                    }
                }
            }
    }
    override fun mergeTable() = request {
        _mergeTable(selectedTableSet.toList())
    }

    private fun disbandGroup(tableNumber: Int) {
        val member = mutableListOf<Int>()
        val group = groupMap[tableNumber]
        groupMap.indices.forEach {
            if (groupMap[it] == group) {
                member.add(it)
                groupMap[it] = it
            }
        }

        var groupColor: ULong = 0u
        _tableList.value = _tableList.value.passMap { list ->
            val newList = list.toMutableList()
            newList.map { table ->
                if(member.contains(table.number.toInt())){
                    groupColor = table.color
                    table.copy(color = defaultColor)
                }
                else table
            }
        }

        groupColorSet.remove(groupColor)
    }

    private suspend fun _payTableWithCash(
        tableNumber: Int
    ) {
        tableService.payTableWithCash(tableNumber)
            .conflate().collect {
                _orderList.value = it.passMap { data ->
                    emptyList()
                }
                disbandGroup(tableNumber)
        }
    }
    override fun payTableWithCash() = request {
        val tableNumber = selectedTableNumber
        _payTableWithCash(tableNumber)
    }

    private suspend fun _payTableWithCard(
        tableNumber: Int
    ) {
        tableService.payTableWithCard(tableNumber)
            .conflate().collect {
                _orderList.value = it.passMap { data ->
                    emptyList()
                }
                disbandGroup(tableNumber)
            }
    }
    override fun payTableWithCard() = request {
        val tableNumber = selectedTableNumber
        _payTableWithCard(tableNumber)
    }

    private suspend fun _payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) {
        tableService.payTableWithPoint(
            tableNumber = tableNumber,
            accountNumber = accountNumber,
            issuedName = issuedName
        ).conflate().collect {
            _orderList.value = it.passMap { data ->
                emptyList()
            }
            disbandGroup(tableNumber)
        }
    }
    override fun payTableWithPoint() = request {
        val tableNumber = selectedTableNumber
        val accountNumber = _pointUse.value.accountNumber.toInt()
        val issuedName = _pointUse.value.userName
        _payTableWithPoint(
            tableNumber,
            accountNumber,
            issuedName
        )
    }

    private suspend fun _addOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) {
        tableService.addOrder(
            tableNumber, menuName, menuPrice
        ).conflate().collect {
            it.passMap { data ->
                _orderList.value = _orderList.value.passMap { list ->
                    val newList = list.toMutableList()
                    val isNewOrder = list
                        .find { order -> order.name == menuName } == null

                    if (isNewOrder) {
                        newList.add(data.convertToUiTableOrder())
                    }
                    else {
                        newList.map { order ->
                            if (order.name == menuName) {
                                data.convertToUiTableOrder()
                            }
                            else
                                order
                        }
                    }

                    newList
                }
            }

        }
    }
    override fun addOrder(name: String, price: String) = request {
        val tableNumber = selectedTableNumber
        val menuPrice = price.toInt()
        _addOrder(tableNumber, name, menuPrice)
    }

    private suspend fun _cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) {
        tableService.cancelOrder(
            tableNumber, menuName, menuPrice
        ).conflate().collect {
            it.passMap { data ->
                _orderList.value = _orderList.value.passMap { list ->
                    val newList = list.toMutableList()
                    val isNoLongerExist = (data.count == 0)

                    if (isNoLongerExist) {
                        newList.removeIf { order -> order.name == menuName }
                    }
                    else {
                        newList.map { order ->
                            if (order.name == menuName) {
                                data.convertToUiTableOrder()
                            }
                            else
                                order
                        }
                    }

                    newList
                }
            }

        }
    }
    override fun cancelOrder(name: String, price: String) = request {
        val tableNumber = selectedTableNumber
        val menuPrice = price.toInt()
        _cancelOrder(tableNumber, name, menuPrice)
    }

    private fun request(job: suspend TableViewModel.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}