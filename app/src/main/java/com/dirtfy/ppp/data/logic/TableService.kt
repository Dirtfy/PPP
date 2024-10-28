package com.dirtfy.ppp.data.logic

import android.util.Log
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataRecord
import com.dirtfy.ppp.data.dto.DataRecordDetail
import com.dirtfy.ppp.data.dto.DataRecordType
import com.dirtfy.ppp.data.dto.DataTableOrder
import com.dirtfy.ppp.data.source.repository.RecordRepository
import com.dirtfy.ppp.data.source.repository.TableRepository
import javax.inject.Inject

class TableService @Inject constructor(
    private val tableRepository: TableRepository,
    private val recordRepository: RecordRepository
): Service {

    private fun isInValidTableNumber(tableNumber: Int): Boolean {
        return tableNumber !in 1..11
    }

    // TODO stream 적용 후 deprecate 시키기
    fun readTables() = operate {
        val tableList = tableRepository.readAllTable()
        tableList
    }

    // TODO stream 적용 후 deprecate 시키기
    fun readOrders(tableNumber: Int) = operate {
        if (isInValidTableNumber(tableNumber))
            throw TableException.InvalidTableNumber()

        val group = tableRepository.readTable(tableNumber).group

        val tableOrderList = tableRepository.readAllOrder(group)
        tableOrderList
    }

    fun tableStream() = tableRepository.tableStream()
    fun orderStream(tableNumber: Int) = tableRepository.orderStream(tableNumber)

    fun mergeTables(tableNumberList: List<Int>) = operate {
        if (tableNumberList.size <= 1)
            throw TableException.NonEnoughMergingTargets()

        for (tableNumber in tableNumberList) {
            if (isInValidTableNumber(tableNumber))
                throw TableException.InvalidTableNumber()
        }

        val tableList = tableRepository.readAllTable()
            .filter { tableNumberList.contains(it.number) }

        val groupUniqueList = tableList.map {
            it.group
        }.toSet().toList()

        val orderCombineList = mutableListOf<DataTableOrder>()
        groupUniqueList.map {
            tableRepository.readAllOrder(it)
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
            tableRepository.deleteAllOrder(it)
        }

        val baseGroup = groupUniqueList[0]

        orderCombineList.forEach {
            tableRepository.createOrder(baseGroup, it.name, it.price)
            tableRepository.updateOrder(baseGroup, it)
        }

        tableList.filter {
            it.group != baseGroup
        }.forEach {
            tableRepository.updateTable(it.copy(group = baseGroup))
        }

        baseGroup
    }

    private suspend fun cleanGroup(group: Int) {
        tableRepository.apply {
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

    fun payTableWithCash(
        tableNumber: Int
    ) = operate {
        val group = tableRepository.readTable(tableNumber).group

        val orderList = tableRepository.readAllOrder(group)

        val payment = recordRepository.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Cash.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
        payment
    }
    fun payTableWithCard(
        tableNumber: Int
    ) = operate {
        val group = tableRepository.readTable(tableNumber).group

        val orderList = tableRepository.readAllOrder(group)

        val payment = recordRepository.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Card.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
        payment
    }
    fun payTableWithPoint(
        tableNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) = operate {
        val group = tableRepository.readTable(tableNumber).group

        val orderList = tableRepository.readAllOrder(group)

        val payment = recordRepository.create(
            record = DataRecord(
                income = -orderList.calcTotalPrice(),
                type = accountNumber.toString(),
                issuedBy = issuedName
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(group)
        payment
    }

    fun addOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) = operate {
        Log.d("WeGlonD", "service addOrder")
        tableRepository.run {
            val group = readTable(tableNumber).group

            val count: Int
            if (isOrderExist(group, menuName)) {
                val order = readOrder(group, menuName)
                count = order.count + 1
                updateOrder(
                    group,
                    order.copy(count = count)
                )
            } else {
                count = 1
                createOrder(
                    group,
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

    fun cancelOrder(
        tableNumber: Int,
        menuName: String,
        menuPrice: Int
    ) = operate {
        tableRepository.run {
            val group = readTable(tableNumber).group

            val count: Int
            if (isOrderExist(group, menuName)) {
                when(val menuCount = readOrder(group, menuName).count) {
                    0 -> { throw TableException.NonEnoughMenuToCancel() }
                    1 -> {
                        count = 0
                        deleteOrder(group, menuName)
                    }
                    else -> {
                        count = menuCount-1
                        updateOrder(
                            group,
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

    fun getGroup(tableNumber: Int) = operate {
        tableRepository.readTable(tableNumber).group
    }
}