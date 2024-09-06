package com.dirtfy.ppp.test.data.logic.impl

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.data.source.repository.TableRepository
import com.dirtfy.ppp.test.data.logic.TableLogic
import com.dirtfy.ppp.test.data.source.RecordSource
import com.dirtfy.ppp.test.data.source.TableSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TableService @Inject constructor(
    private val tableSource: TableSource,
    private val recordSource: RecordSource
): TableLogic {

    private fun isInValidTableNumber(tableNumber: Int): Boolean {
        return tableNumber !in 1..11
    }

    override fun readTables() = flow<List<DataTable>> {
        tableSource.readAllTable()
    }

    override fun readOrders(tableNumber: Int) = flow<List<DataTableOrder>> {
        if (isInValidTableNumber(tableNumber))
            throw TableException.InvalidTableNumber()

        val group = tableSource.readTable(tableNumber).group

        tableSource.readAllOrder(group)
    }

    override fun mergeTables(tableNumberList: List<Int>) = flow<Unit> {
        if (tableNumberList.size <= 1)
            throw TableException.NonEnoughMergingTargets()

        for (tableNumber in tableNumberList) {
            if (isInValidTableNumber(tableNumber))
                throw TableException.InvalidTableNumber()
        }

        val tableList = tableSource.readAllTable()
            .filter { tableNumberList.contains(it.number) }

        val groupUniqueList = tableList.map {
            it.group
        }.toSet().toList()

        val orderCombineList = mutableListOf<DataTableOrder>()
        groupUniqueList.map {
            tableSource.readAllOrder(it)
        }.forEach { list ->
            list.forEach { order ->
                val hasSameMenu = orderCombineList
                    .find { added -> added.name == order.name } != null

                if (hasSameMenu) {
                    orderCombineList.replaceAll { added ->
                        if (added.name == order.name)
                            added.copy(count = added.count + order.count)
                        else
                            added
                    }
                }
                else {
                    orderCombineList.add(order)
                }
            }
        }

        groupUniqueList.forEach {
            tableSource.deleteAllOrder(it)
        }

        val baseGroup = groupUniqueList[0]

        orderCombineList.forEach {
            tableSource.createOrder(baseGroup, it.name, it.price)
            tableSource.updateOrder(baseGroup, it)
        }

        tableList.filter {
            it.group != baseGroup
        }.forEach {
            tableSource.updateTable(it.copy(group = baseGroup))
        }
    }

    private suspend fun cleanGroup(group: Int) {
        tableSource.apply {
            deleteAllOrder(group)
            readAllTable()
                .filter { it.group == group }
                .forEach {
                    updateTable(
                        it.copy(group = it.number)
                    )
                }
        }
    }

    private fun List<DataTableOrder>.calcTotalPrice(): Int {
        return this.sumOf { it.count * it.price }
    }
    private fun DataTableOrder.convertToRecordDetail(): DataRecordDetail {
        return DataRecordDetail(
            name = name,
            amount = price,
            count = count
        )
    }

    override fun payTableWithCash(
        tableNumber: Int
    ) = flow<Unit> {
        val group = tableSource.readTable(tableNumber).group

        val orderList = tableSource.readAllOrder(group)

        recordSource.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Cash.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
    }
    override fun payTableWithCard(
        tableNumber: Int
    ) = flow<Unit> {
        val group = tableSource.readTable(tableNumber).group

        val orderList = tableSource.readAllOrder(group)

        recordSource.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Card.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
    }
    override fun payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) = flow<Unit> {
        val group = tableSource.readTable(tableNumber).group

        val orderList = tableSource.readAllOrder(group)

        recordSource.create(
            record = DataRecord(
                income = -orderList.calcTotalPrice(),
                type = accountNumber.toString(),
                issuedBy = issuedName
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
    }

    override fun addOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) = flow<DataTableOrder> {
        tableSource.run {
            val group = readTable(tableNumber).group

            val count: Int
            if (isOrderExist(group, menuName)) {
                val order = readOrder(tableNumber, menuName)
                count = order.count + 1
                updateOrder(
                    tableNumber,
                    order.copy(count = count)
                )
            } else {
                count = 1
                createOrder(
                    tableNumber,
                    menuName,
                    menuPrice
                )
            }

            DataTableOrder(
                menuName,
                menuPrice,
                count
            )
        }
    }
    override fun cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) = flow<DataTableOrder> {
        tableSource.run {
            val group = readTable(tableNumber).group

            val count: Int
            if (isOrderExist(group, menuName)) {
                when(val menuCount = readOrder(group, menuName).count) {
                    0 -> { throw TableException.NonEnoughMenuToCancel() }
                    1 -> {
                        count = 0
                        deleteOrder(tableNumber, menuName)
                    }
                    else -> {
                        count = menuCount-1
                        updateOrder(
                            tableNumber,
                            DataTableOrder(
                                menuName,
                                menuPrice,
                                count
                            )
                        )
                    }
                }
            }
            else throw TableException.InvalidOrderName()

            DataTableOrder(
                menuName,
                menuPrice,
                count
            )
        }

    }

    override fun getGroup(tableNumber: Int) = flow<Int> {
        tableSource.readTable(tableNumber).group
    }

    override fun tableStream() = tableSource.tableStream()

    override fun tableOrderStream(tableNumber: Int)
    = tableSource.tableOrderStream(tableNumber)
}