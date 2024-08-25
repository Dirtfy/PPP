package com.dirtfy.ppp.data.logic

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.dto.DataTable
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.data.source.repository.TableRepository

class TableService(
    val tableRepository: TableRepository,
    val recordRepository: RecordRepository
): Service {

    private fun isInValidTableNumber(tableNumber: Int): Boolean {
        return tableNumber !in 1..11
    }

    fun readTables() = asFlow {
        tableRepository.readTables()
    }

    fun readOrders(tableNumber: Int) = asFlow {
        if (isInValidTableNumber(tableNumber))
            throw TableException.InvalidTableNumber()

        val group = tableRepository.getGroupNumber(tableNumber)

        tableRepository.readOrders(group)
    }

    fun mergeTables(tableNumberList: List<Int>) = asFlow {
        if (tableNumberList.size <= 1)
            throw TableException.NonEnoughMergingTargets()

        for (tableNumber in tableNumberList) {
            if (isInValidTableNumber(tableNumber))
                throw TableException.InvalidTableNumber()
        }

        val groupUniqueList = tableNumberList.map {
            tableRepository.getGroupNumber(it)
        }.toSet().toList()

        val groupAndMemberPairList = groupUniqueList
            .map {
                tableRepository.getMemberTableNumbers(it)
            }
            .zip(groupUniqueList)

        val baseGroup = groupAndMemberPairList
            .maxByOrNull {
                it.first.size
            }?.second?: throw TableException.GroupLoss()

        groupUniqueList
            .filter {
                it != baseGroup
            }
            .forEach {
                tableRepository.mergeGroup(baseGroup, it)
            }
    }

    private suspend fun cleanGroup(group: Int) {
        val memberList = tableRepository.getMemberTableNumbers(group)
        memberList.forEach {
            tableRepository.deleteAllOrder(it)
            tableRepository.updateTable(DataTable(
                number = it,
                group = it
            ))
        }
    }

    private fun List<DataTableOrder>.calcTotalPrice(): Int {
        return this.sumOf { it.count * it.price }
    }
    private fun List<DataTableOrder>.convertToRecordDetailList(): List<DataRecordDetail> {
        return this.map {
            DataRecordDetail(
                name = it.name,
                amount = it.price,
                count = it.count
            )
        }
    }

    fun payTableWithCash(
        tableNumber: Int,
        orderList: List<DataTableOrder>
    ) = asFlow {
        recordRepository.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Cash.name
            ),
            detailList = orderList.convertToRecordDetailList()
        )
        // TODO 다른 DB간 transaction 처리
        val group = tableRepository.getGroupNumber(tableNumber)
        cleanGroup(group)
    }
    fun payTableWithCard(
        tableNumber: Int,
        orderList: List<DataTableOrder>
    ) = asFlow {
        recordRepository.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Card.name
            ),
            detailList = orderList.convertToRecordDetailList()
        )
        // TODO 다른 DB간 transaction 처리
        val group = tableRepository.getGroupNumber(tableNumber)
        cleanGroup(group)
    }
    fun payTableWithPoint(
        tableNumber: Int,
        orderList: List<DataTableOrder>,
        accountNumber: Int,
        issuedName: String
    ) = asFlow {
        recordRepository.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = accountNumber.toString(),
                issuedBy = issuedName
            ),
            detailList = orderList.convertToRecordDetailList()
        )
        // TODO 다른 DB간 transaction 처리
        val group = tableRepository.getGroupNumber(tableNumber)
        cleanGroup(group)
    }

    fun addOrder(
        tableNumber: Int,
        order: DataTableOrder
    ) = asFlow {
        val group = tableRepository.getGroupNumber(tableNumber)
        val menuCount = tableRepository.getMenuCount(group, order.name)

        if (menuCount == 0) {
            tableRepository.createOrder(
                tableNumber,
                order.name,
                order.price
            )
        } else {
            tableRepository.updateOrder(
                tableNumber,
                order
            )
        }
    }
    fun cancelOrder(
        tableNumber: Int,
        order: DataTableOrder
    ) = asFlow {
        val group = tableRepository.getGroupNumber(tableNumber)
        val menuCount = tableRepository.getMenuCount(group, order.name)

        when(menuCount) {
            0 -> { throw TableException.NonEnoughMenuToCancel() }
            1 -> { tableRepository.deleteOrder(tableNumber, order.name) }
            else -> {
                tableRepository.updateOrder(
                    tableNumber,
                    order
                )
            }
        }
    }
}