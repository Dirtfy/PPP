package com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.logic.TableBusinessLogic
import com.dirtfy.ppp.ui.controller.common.converter.feature.table.TableAtomConverter.convertToUiTable
import com.dirtfy.ppp.ui.controller.feature.table.TableListController
import com.dirtfy.ppp.ui.state.common.UiScreenState
import com.dirtfy.ppp.ui.state.common.UiState
import com.dirtfy.ppp.ui.state.feature.table.UiTableMergeScreenState
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTable
import com.dirtfy.ppp.ui.state.feature.table.atom.UiTableMode
import com.dirtfy.tagger.Tagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

class TableListControllerImpl @Inject constructor(
    private val tableBusinessLogic: TableBusinessLogic
): TableListController, Tagger {

    private val groupColorSet = mutableSetOf<ULong>()
    private val defaultColor = Color.LightGray.value

    private val tableFormation: List<Int> = listOf(
        11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 1
    )
    private val selectedTableSet: MutableSet<Int> = mutableSetOf()
    private val groupMap: MutableList<Int> = MutableList(12) { it }

    private val retryTrigger: MutableStateFlow<Int> = MutableStateFlow(0)

    private val tableListFlow: Flow<List<UiTable>> = retryTrigger
        .flatMapLatest {
            Log.d(TAG, "tableListFlow - flatMapLatest")
            tableBusinessLogic.tableStream()
                .map { list ->
                    Log.d(TAG, _screenData.value.toString())
                    setTableListState(UiScreenState(UiState.COMPLETE))
                    val tableList = _updateTableList(list)
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

    private val _screenData: MutableStateFlow<UiTableMergeScreenState> =
        MutableStateFlow(UiTableMergeScreenState())
    override val screenData: Flow<UiTableMergeScreenState> = _screenData
        .combine(tableListFlow) { state, tableList ->
            var newState = state.copy(
                sourceTableList = tableList,
                tableListState = state.tableListState
            )

            if (state.tableList == emptyList<UiTable>() && tableList != emptyList<UiTable>()
                || state.tableList !== tableList && state.mode == UiTableMode.Main)
                newState = newState.copy(
                    tableList = tableList.map { table -> table },
                )

            Log.d(TAG, "screenData\n$newState")
            _screenData.update { newState }
            newState
        }

    override fun retryUpdateTableList() {
        Log.d(TAG, "retryUpdateTableList")
        retryTrigger.value += 1
    }

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

        setTableListState(UiScreenState(UiState.COMPLETE))
        return tableFormation.map { tableNumber ->
            if (tableNumber == 0)
                UiTable("0", Color.Transparent.value)
            else {
                Log.d(TAG, "$tableNumber")
                val uiTable = newList.find { table ->
                    table.number == tableNumber.toString()
                }
                if (uiTable == null) {
                    // TODO Transaction 구현 후 주석 해제 해보자.
                    // setTableListState(UiScreenState(UiState.FAIL, TableException.NumberLoss()))

                    // TODO 일단 더미 테이블로 바꾸기. 오류 처리 방법 논의 필요.
                    UiTable("?", defaultColor)
                } else uiTable
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
        val group = groupMap[tableNumber]
        val member = getMembersOf(group)

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
        val group = groupMap[tableNumber]
        val member = getMembersOf(group)

        var tableList = _screenData.value.tableList.map {
            val nowColor = Color(it.color)
            it.copy(color = nowColor.copy(alpha = 1f).value)
        }

        if (selectedTableSet.contains(tableNumber)) {
            Log.d(TAG, "table $tableNumber is already selected")
            setMode(UiTableMode.Main)
//            selectedTableSet.removeAll(member.toSet()) // setMode 에서 해줌.
            Color(table.color).copy(alpha = 1f)
        } else {
            Log.d(TAG, "table $tableNumber is not selected yet")
            setMode(UiTableMode.Order)
            selectedTableSet.clear()
            selectedTableSet.addAll(member.toSet())
            val newColor = Color(table.color).copy(alpha = 0.5f)

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

    private fun getMembersOf(group: Int): MutableList<Int> {
        val member = mutableListOf<Int>()
        groupMap.indices.forEach {
            if (groupMap[it] == group)
                member.add(it)
        }
        return member
    }

    private suspend fun _mergeTable(tableList: List<Int>) {
        _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.LOADING)) }
        tableBusinessLogic.mergeTables(tableList)
            .catch { cause ->
                Log.e(TAG, "table merge failed - ${cause.message}")
                _screenData.update { it.copy(mergeTableState = UiScreenState(UiState.FAIL, cause)) }
            }
            .conflate().collect { groupNumber ->
                // TODO 아래 코드 state 변경만 빼고 다 지우고 syncTableList() 호출하면 바로 아래 색깔 문제도 해결.
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
        selectedTableSet.clear()
    }

    override fun disbandGroup(tableNumber: Int) {
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

        val groupColor = _screenData.value.tableList.find { it.number.toInt() == tableNumber }!!.color
        groupColorSet.remove(groupColor)
    }

    override fun setMode(mode: UiTableMode) {
        _screenData.update { it.copy(mode = mode) }
        if (mode == UiTableMode.Main) {
            selectedTableSet.clear()
            syncTableList()
        }
    }

    override fun setTableListState(state: UiScreenState) {
        _screenData.update { it.copy(tableListState = state) }
    }

    override fun setMergeTableState(state: UiScreenState) {
        _screenData.update { it.copy(mergeTableState = state) }
    }
}