package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.api.impl.feature.menu.firebase.MenuFireStore
import com.dirtfy.ppp.data.api.impl.feature.record.firebase.RecordFireStore
import com.dirtfy.ppp.data.api.impl.feature.table.firebase.TableFireStore
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.logic.MenuBusinessLogic
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.common.StringFormatConverter
import com.dirtfy.ppp.ui.controller.common.converter.feature.menu.MenuAtomConverter.convertToUiMenu
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTable
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTableOrder
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.menu.atom.UiMenu
import com.dirtfy.ppp.ui.state.feature.table.UiTableScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiPointUse
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class TableViewModel: ViewModel(), TableController, Tagger {

    private val tableService: TableBusinessLogic = TableBusinessLogic(TableFireStore(), RecordFireStore())
    private val menuService: MenuBusinessLogic = MenuBusinessLogic(MenuFireStore())

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

    private val _screenData: MutableStateFlow<UiTableScreenState>
    = MutableStateFlow(UiTableScreenState())

    private val tableListFlow = tableService.tableStream()
        .catch { cause ->
            Log.e(TAG, "tableList load failed")
            _screenData.update { it.copy(tableListState = UiScreenState(UiState.FAIL, cause.message)) }
        }
        .map {
            Log.d(TAG, _screenData.value.toString())
            val tableList = _updateTableList(it)
            tableList
        }

    private lateinit var orderListFlow: Flow<List<UiTableOrder>>

    private val menuListFlow = menuService.menuStream()
        .catch { cause ->
            Log.e(TAG, "menuList load failed")
            _screenData.update { it.copy(menuListState = UiScreenState(UiState.FAIL, cause.message)) }
        }
        .map {
            val menuList = it.map { data -> data.convertToUiMenu() }
            menuList
        }

    override val screenData: StateFlow<UiTableScreenState>
        = _screenData
        .combine(tableListFlow) { state, tableList ->
            Log.d(TAG, "screenData combine 1")

            var newState = state.copy(
                // TODO 논의필요: DB에서의 테이블 변경과 로컬에서의 변경을 따로 관리하도록 함.
                sourceTableList = tableList
            )
            if (state.tableList != tableList /* 내용이 달라졌을 때 */
                || state.tableList !== tableList /* 내용이 같지만 다른 인스턴스 */
                || tableList == emptyList<UiTable>() /* emptyList()는 항상 같은 인스턴스 */)
                newState = newState.copy(
                    tableListState = UiScreenState(state = UiState.COMPLETE)
                )

            if (state.tableList == emptyList<UiTable>())
                _screenData.update { it.copy(tableList = tableList, tableListState = UiScreenState(
                    UiState.COMPLETE)
                ) }

            newState
        }.combine(menuListFlow) { state, menuList ->
            Log.d(TAG, "screenData combine 2")
            var newState = state.copy(
                menuList = menuList
            )
            if (state.menuList != menuList /* 내용이 달라졌을 때 */
                || state.menuList !== menuList /* 내용이 같지만 다른 인스턴스 */
                || menuList == emptyList<UiMenu>() /* emptyList()는 항상 같은 인스턴스 */)
                newState = newState.copy(
                    menuListState = UiScreenState(state = UiState.COMPLETE)
                )
            newState
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _screenData.value
        )

    private fun _updateTableList(dbTableList: List<DataTable>): List<UiTable> {
        val newList = dbTableList.map { data -> data.convertToUiTable() }.toMutableList()
        val groupMemberCount = MutableList(12) { 0 }
        dbTableList.forEach { table ->
            groupMap[table.number] = table.group
            groupMemberCount[table.group]++
        }

        groupMemberCount
            .zip((0..11))
            .filter { zip -> zip.first > 1 }
            .map { zip -> zip.second }
            .forEach { group ->
                var groupColor = getRandomColor()
                while (groupColorSet.contains(groupColor)) {
                    groupColor = getRandomColor()
                }
                groupColorSet.add(groupColor)

                (1..11).filter { tableNumber ->
                    groupMap[tableNumber] == group
                }.let { member ->
                    newList.replaceAll { table ->
                        if (member.contains(table.number.toInt()))
                            table.copy(color = groupColor)
                        else table
                    }
                }
            }

        return tableFormation.map { tableNumber ->
            if (tableNumber == 0)
                UiTable("0", Color.Transparent.value)
            else {
                Log.d(TAG, "$tableNumber")
                val uiTable = newList.find { table ->
                    table.number == tableNumber.toString()
                }
                if (uiTable == null) {
                    _screenData.update {
                        it.copy(
                            tableListState = UiScreenState(UiState.FAIL, TableException.NumberLoss().message)
                        )
                    }
                    // TODO 일단 더미 테이블로 바꾸기. 오류 처리 방법 논의 필요.
                    UiTable("?", defaultColor)
                }
                else uiTable
            }
        }
    }

    // TODO deprecate
    override suspend fun updateTableList() {
//        _updateTableList()
    }

    private suspend fun _updateOrderList(tableNumber: Int) {
        _screenData.update {
            it.copy(
                orderListState = UiScreenState(UiState.LOADING),
                orderTotalPrice = StringFormatConverter.formatCurrency(0)
            )
        }

        orderListFlow = tableService.orderStream(tableNumber).map { it.map { data -> data.convertToUiTableOrder() } }

        orderListFlow
            .catch { cause ->
                Log.e(TAG, "orderList load failed")
                _screenData.update {
                    it.copy(
                        orderListState = UiScreenState(UiState.FAIL, cause.message)
                    )
                }
            }
            .conflate().collect {
                _screenData.update { before ->
                    before.copy(
                        orderList = it,
                        orderListState = UiScreenState(UiState.COMPLETE),
                        orderTotalPrice = StringFormatConverter.formatCurrency(
                            it.sumOf { order -> StringFormatConverter.parseCurrency(order.price) * order.count.toInt() }
                        )
                    )
                }
            }
    }
    override suspend fun updateOrderList(table: UiTable) {
        val tableNumber = table.number.toInt()
        _updateOrderList(tableNumber)
    }

    private suspend fun _updateMenuList() {
        menuService.readMenu().conflate().collect { menuList ->
            _screenData.update {
                it.copy(
                    menuList = menuList.map {
                        data -> data.convertToUiMenu()
                    }
                )
            }
        }
    }

    // TODO deprecate
    override suspend fun updateMenuList() {
//        _updateMenuList()
    }

    override fun updatePointUse(pointUse: UiPointUse) {
        _screenData.update { it.copy(pointUse = pointUse) }
    }

    private fun _clickTableOnMergeMode(
        table: UiTable,
        member: List<Int>
    ) {
        val tableNumber = table.number.toInt()

        val newColor = if (selectedTableSet.contains(tableNumber)) {
            selectedTableSet.removeAll(member.toSet())
            Color(table.color).copy(alpha = 1f)
        } else {
            selectedTableSet.addAll(member.toSet())
            // TODO 색이 안 연해짐
            Color(table.color).copy(alpha = 0.5f)
        }

        _screenData.update { before ->
            before.copy(
                tableList = _screenData.value.tableList.map { nowTable ->
                    if (member.contains(nowTable.number.toInt())) {
                        nowTable.copy(color = newColor.value)
                    } else {
                        nowTable
                    }
                }
            )
        }
    }

    private fun _clickedTable(
        table: UiTable,
        member: List<Int>
    ) {
        val tableNumber = table.number.toInt()

        var tableList = _screenData.value.tableList.map {
            val nowColor = Color(it.color)
            it.copy(color = nowColor.copy(alpha = 1f).value)
        }

        val newColor = if (selectedTableSet.contains(tableNumber)) {
            Log.d(TAG, "table $tableNumber is already selected")
            selectedTableSet.removeAll(member.toSet()) // 왜 clear가 아니라 removeAll?
//            _screenData.update { before -> before.copy(mode = UiTableMode.Main) }
            Color(table.color).copy(alpha = 1f)
        } else {
            Log.d(TAG, "table $tableNumber is not selected yet")
            selectedTableSet.clear()
            selectedTableSet.addAll(member.toSet())
//            _screenData.update { before -> before.copy(mode = UiTableMode.Order) }
            // TODO 색이 안 연해짐
            Color(table.color).copy(alpha = 0.5f)
        }
        Log.d(TAG, "newColor.alpha: ${newColor.alpha}")

        Log.d(TAG, "tableList size: ${tableList.size}")
        tableList = tableList.map { nowTable ->
            Log.d(TAG, "$nowTable")
            if (member.contains(nowTable.number.toInt())) {
                Log.d(TAG, "this is a member of the group")
                nowTable.copy(color = newColor.value)
            } else {
                Log.d(TAG, "this is NOT a member of the group")
                nowTable
            }
        }

        _screenData.update { it.copy(tableList = tableList) }

        if (newColor.alpha > 0.7f)
            setMode(UiTableMode.Main)
        else
            setMode(UiTableMode.Order)
    }
    override fun clickTable(table: UiTable) {
        val tableNumber = table.number.toInt()
        val group = groupMap[tableNumber]
        val member = mutableListOf<Int>()
        groupMap.indices.forEach {
            if (groupMap[it] == group)
                member.add(it)
        }
        Log.d(TAG, "clickTable - table: $tableNumber, group: $group")
        Log.d(TAG, "${member.size} member")
        member.forEach { Log.d(TAG, "$it ") }

        if (_screenData.value.mode == UiTableMode.Merge)
            _clickTableOnMergeMode(table, member)
        else
            _clickedTable(table, member)
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
        _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.LOADING)) }
        tableService.mergeTables(tableList)
            .catch { cause ->
                Log.e(TAG, "table merge failed - ${cause.message}")
                _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.FAIL, cause.message)) }
            }
            .conflate().collect { groupNumber ->
                val newList = _screenData.value.tableList
                var groupColor = getRandomColor()
                while (groupColorSet.contains(groupColor)) {
                    groupColor = getRandomColor()
                }
                groupColorSet.add(groupColor)

                groupColor = Color(groupColor).value

                selectedTableSet.clear()

                _screenData.update {
                    it.copy(
                        // TODO 이걸 하면 머지 끝났을 때 머지 모드에서 색깔이 바뀜 but 머지 모드 해제하면 색깔이 바뀜
                        // 색깔이 바뀌는 이유는 stream 이 변경사항을 가져올 때 마다 랜덤 색깔 생성 후 지정하기 때문.
                        // 이후 랜덤 컬러가 아닌 컬러 프리셋 정하고 갖고와서 하는 걸로 바꾸면 해결 가능
                        tableList = newList.map { table ->
                            if (tableList.contains(table.number.toInt())){
                                groupMap[table.number.toInt()] = groupNumber
                                table.copy(color = groupColor)
                            }
                            else
                                table
                        },
                        mergeTableState = UiScreenState(UiState.COMPLETE)
                    )
                }
            }
    }
    override suspend fun mergeTable() {
        _mergeTable(selectedTableSet.toList())
    }

    override fun cancelMergeTable() {
        val tableList = _screenData.value.tableList.map { nowTable ->
            val nowColor = Color(nowTable.color)
            nowTable.copy(color = nowColor.copy(alpha = 1f).value)
        }
        selectedTableSet.clear()
        _screenData.update { it.copy(tableList = tableList) }
        setMode(UiTableMode.Main)
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

        if (selectedTableSet.contains(tableNumber))
            selectedTableSet.removeAll(member.toSet())

        var groupColor: ULong = 0u

        _screenData.update { it.copy(
            tableList = it.tableList.map { table ->
                if(member.contains(table.number.toInt())){
                    groupColor = table.color
                    table.copy(color = defaultColor)
                }
                else table
            }
        ) }

        groupColorSet.remove(groupColor)
        setMode(UiTableMode.Main)
    }

    private suspend fun _payTableWithCash(
        tableNumber: Int
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableService.payTableWithCash(tableNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
                disbandGroup(tableNumber)
            }
    }
    override suspend fun payTableWithCash() {
        val tableNumber = selectedTableNumber
        _payTableWithCash(tableNumber)
    }

    private suspend fun _payTableWithCard(
        tableNumber: Int
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableService.payTableWithCard(tableNumber)
            .catch { cause ->
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
            }
            .conflate().collect {
                _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
                disbandGroup(tableNumber)
            }
    }
    override suspend fun payTableWithCard() {
        val tableNumber = selectedTableNumber
        _payTableWithCard(tableNumber)
    }

    private suspend fun _payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) {
        _screenData.update { it.copy(payTableState = UiScreenState(UiState.LOADING)) }
        tableService.payTableWithPoint(
            tableNumber = tableNumber,
            accountNumber = accountNumber,
            issuedName = issuedName
        ).catch { cause ->
            _screenData.update { it.copy(payTableState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect {
            _screenData.update { it.copy(payTableState = UiScreenState(UiState.COMPLETE)) }
            disbandGroup(tableNumber)
        }
    }
    override suspend fun payTableWithPoint() {
        val tableNumber = selectedTableNumber
        val accountNumber = _screenData.value.pointUse.accountNumber.toInt()
        val issuedName = _screenData.value.pointUse.userName
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
        Log.d("WeGlonD", "viewmodel addorder")
        _screenData.update { it.copy(addOrderState = UiScreenState(UiState.LOADING)) }
        tableService.addOrder(
            tableNumber, menuName, menuPrice
        ).catch { cause ->
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect { data ->
            _screenData.update { it.copy(addOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun addOrder(name: String, price: String) {
        val tableNumber = selectedTableNumber
        val menuPrice = StringFormatConverter.parseCurrency(price)
        _addOrder(tableNumber, name, menuPrice)
    }

    private suspend fun _cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) {
        _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.LOADING)) }
        tableService.cancelOrder(
            tableNumber, menuName, menuPrice
        ).catch { cause ->
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.FAIL, cause.message)) }
        }.conflate().collect {
            _screenData.update { it.copy(cancelOrderState = UiScreenState(UiState.COMPLETE)) }
        }
    }
    override suspend fun cancelOrder(name: String, price: String) {
        val tableNumber = selectedTableNumber
        val menuPrice = StringFormatConverter.parseCurrency(price)
        _cancelOrder(tableNumber, name, menuPrice)
    }

    override fun setMode(mode: UiTableMode) {
        _screenData.update {
            it.copy(
                mode = mode,
                // TODO 논의필요: 메인 모드로 돌아 갈 때 서버에서 받아온 변경사항 적용
                tableList = if (mode == UiTableMode.Main) it.sourceTableList.map { it } else it.tableList
            )
        }
    }

    override fun request(job: suspend TableController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}