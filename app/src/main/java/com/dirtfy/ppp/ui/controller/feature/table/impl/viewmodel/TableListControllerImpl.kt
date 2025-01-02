package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.api.TableApi.Companion.TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToDataTableOrder
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTable
import com.dirtfy.ppp.ui.controller.feature.table.TableListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.UiTableListScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableOrder
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

class TableListControllerImpl @Inject constructor(
    private val tableBusinessLogic: TableBusinessLogic
): TableListController, Tagger {

    private val defaultColor = Color.LightGray.value

    private val tableFormation: List<Int> = listOf(
        11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 1
    )
    private val selectedTableSet: MutableSet<Int> = mutableSetOf()
    private val groupMap: MutableMap<Int, Int> = mutableMapOf()

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS

    private val retryTrigger: MutableStateFlow<Int> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tableListFlow: Flow<List<UiTable>> = retryTrigger
        .flatMapLatest {
            Log.d(TAG, "tableListFlow - flatMapLatest")
            tableBusinessLogic.tableStream()
                .map { list ->
                    Log.d(TAG, _screenData.value.toString())
                    val tableList = _updateTableList(list)
                    setTableListState(UiScreenState(UiState.COMPLETE))
                    tableList
                }
                .onStart {
                    setTableListState(UiScreenState(UiState.LOADING))
                    emit(emptyList())
                }
                .catch { cause ->
                    Log.e(TAG, "tableList load failed")
                    setTableListState(UiScreenState(UiState.FAIL, cause))
                    emit(emptyList())
                }
        }

    private val _screenData: MutableStateFlow<UiTableListScreenState> =
        MutableStateFlow(UiTableListScreenState())
    override val screenData: Flow<UiTableListScreenState> = _screenData
        .combine(tableListFlow) { state, tableList ->
            var newState = state.copy(
                sourceTableList = tableList,
                tableListState = state.tableListState
            )

            if (state.tableList == emptyList<UiTable>() && tableList != emptyList<UiTable>()
                || state.tableList != tableList && state.mode == UiTableMode.Main)
                newState = newState.copy(
                    tableList = tableList.map { table -> table },
                )

            if (state.mode == UiTableMode.Merge && state.sourceTableList != tableList && state.mergeTableState.state == UiState.LOADING) {
                newState = newState.copy(
                    tableList = tableList.map { table -> table },
                    mergeTableState = UiScreenState(UiState.COMPLETE),
                )
            }

            Log.d(TAG, "screenData\n$newState")
            _screenData.update { newState }
            newState
        }

    override fun retryUpdateTableList() {
        Log.d(TAG, "retryUpdateTableList")
        retryTrigger.value += 1
    }

    override fun getGroupNumber(tableNumber: Int): Int {
        return groupMap[tableNumber] ?: DataTable.GROUP_NOT_ASSIGNED
    }

    override fun getGroup(groupNumber: Int): DataTableGroup {
        val member = groupMap.filter { it.value == groupNumber }.map { it.key }
        return DataTableGroup(groupNumber, member)
    }

    private fun _updateTableList(dbTableList: List<DataTable>): List<UiTable> {
        val newList = dbTableList.map { data -> data.convertToUiTable() }.toMutableList()
        val groupColorSet = mutableSetOf<ULong>()

        val groupColorsMap = dbTableList
            .map { it.group } //group 번호와 color 매칭
            .distinct()
            .associateWith {
                var groupColor = getRandomColor()
                while (groupColorSet.contains(groupColor)) {
                    groupColor = getRandomColor()
                }
                groupColorSet.add(groupColor)
                groupColor
            }

        groupMap.clear()
        for (idx in dbTableList.indices) {
            val dataTable = dbTableList[idx]
            val color = groupColorsMap[dataTable.group]
            newList[idx].color = color!!
            groupMap[dataTable.number] = dataTable.group
        }

        return tableFormation.map { tableNumber ->
            if (tableNumber == 0)
                UiTable("0", Color.Transparent.value)
            else {
                Log.d(TAG, "$tableNumber")
                val uiTable = newList.find { table ->
                    table.number == tableNumber.toString()
                } ?: UiTable("$tableNumber", defaultColor)
                uiTable
            }
        }
    }

    @Deprecated("screen state synchronized with repository")
    override suspend fun updateTableList() {
//        _updateTableList()
    }

    override fun syncTableList() {
        _screenData.update { before ->
            before.copy(tableList = before.sourceTableList.map { it })
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

    override fun clickTableOnMergeMode(table: UiTable) {
        val tableNumber = table.number.toInt()
        val group = getGroupNumber(tableNumber)
        val member = getMembersOf(group)
        if (member.isEmpty()) member.add(tableNumber)

        val newColor = if (selectedTableSet.contains(tableNumber)) {
            selectedTableSet.removeAll(member.toSet())
            Color(table.color).copy(alpha = 1f)
        } else {
            selectedTableSet.addAll(member.toSet())
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

    override fun clickTableOnMainOrOrderMode(table: UiTable) {
        val tableNumber = table.number.toInt()
        val group = getGroupNumber(tableNumber)
        val member = getMembersOf(group)
        if (member.isEmpty()) member.add(tableNumber)

        if (selectedTableSet.contains(tableNumber) || table.color == defaultColor) {
            Log.d(TAG, "table $tableNumber is already selected")
            setMode(UiTableMode.Main)
//            selectedTableSet.removeAll(member.toSet()) // setMode 에서 해줌.
//            Color(table.color).copy(alpha = 1f)
        } else {
            Log.d(TAG, "table $tableNumber is not selected yet")
            setMode(UiTableMode.Order)
            selectedTableSet.clear()
            selectedTableSet.addAll(member.toSet())
            val newColor = Color(table.color).copy(alpha = 0.5f)

            var tableList = _screenData.value.tableList.map {
                val nowColor = Color(it.color)
                it.copy(color = nowColor.copy(alpha = 1f).value)
            }
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

        }
    }

    override suspend fun trySetMergeMode() {
        setTrySetMergeModeState(UiScreenState(UiState.LOADING))
        tableBusinessLogic.getTableGroupLock()
            .catch { cause ->
                setTrySetMergeModeState(UiScreenState(UiState.FAIL, cause))
            }.collect {
                setTrySetMergeModeState(UiScreenState(UiState.COMPLETE))
                _screenData.update { it.copy(mode = UiTableMode.Merge) }
                startSessionTimer()
            }
    }

    private fun startSessionTimer() {
        timer?.cancel()
        timeLeftInMillis = TABLE_GROUP_LOCK_EXPIRE_TIME_MILLISECONDS
        updateSessionTimerText()

        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateSessionTimerText()
            }

            override fun onFinish() {
                setMode(UiTableMode.Main)
            }
        }.start()
    }

    private fun updateSessionTimerText() {
        val timeLeftInSeconds = (timeLeftInMillis / 1000).toInt()
        _screenData.update { it.copy(timeLeftUntilEndOfMergeMode = "$timeLeftInSeconds") }
    }

    override suspend fun escapeFromMergeMode() {
        setEscapeFromMergeModeState(UiScreenState(UiState.LOADING))
        tableBusinessLogic.releaseTableGroupLock()
            .catch { cause ->
                setEscapeFromMergeModeState(UiScreenState(UiState.FAIL, cause))
            }.collect {
                setEscapeFromMergeModeState(UiScreenState(UiState.COMPLETE))
                setMode(UiTableMode.Main)
            }
    }

    private fun getMembersOf(groupNum: Int): MutableList<Int> {
        val member = mutableListOf<Int>()
        groupMap.forEach { (table, group) ->
            if (group == groupNum)
                member.add(table)
        }
        return member
    }

    private suspend fun _mergeTable(tableNumberList: List<Int>) {
        _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.LOADING)) }
        val dataTableList = tableNumberList.map {
            DataTable(
                number = it,
                group = getGroupNumber(it)
            )
        }
        Log.d("WeGlonD", "controller - mergeTable - dataTableList")
        dataTableList.forEach { Log.d("WeGlonD", it.toString()) }
        tableBusinessLogic.mergeTables(dataTableList)
            .catch { cause ->
                Log.e(TAG, "table merge failed - ${cause.message}")
                _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.FAIL, cause)) }
            }
            .collect { mergedGroupNum ->
                selectedTableSet.clear()
                Log.d("WeGlonD", "merged group number: $mergedGroupNum")
                startSessionTimer()
            }
    }
    override suspend fun mergeTable() {
        _mergeTable(selectedTableSet.toList())
    }

    override fun cancelMergeTable() {
        selectedTableSet.clear()
    }

    override suspend fun dissolveGroup(groupNumber: Int, orderList: List<UiTableOrder>) {
        tableBusinessLogic.dissolveGroup(groupNumber, orderList.map { it.convertToDataTableOrder() })
    }

    override fun setMode(mode: UiTableMode) {
        if (mode == UiTableMode.Merge) throw TableException.IllegalMergeModeSet()

        _screenData.update { it.copy(mode = mode) }
        if (mode == UiTableMode.Main) {
            selectedTableSet.clear()
            syncTableList()
        }
    }

    override fun setTableListState(state: UiScreenState) {
        _screenData.update { it.copy(tableListState = state) }
    }

    override fun setTrySetMergeModeState(state: UiScreenState) {
        _screenData.update { it.copy(trySetMergeModeState = state) }
    }

    override fun setEscapeFromMergeModeState(state: UiScreenState) {
        _screenData.update { it.copy(escapeFromMergeModeState = state) }
    }

    override fun setMergeTableState(state: UiScreenState) {
        _screenData.update { it.copy(mergeTableState = state) }
    }
}