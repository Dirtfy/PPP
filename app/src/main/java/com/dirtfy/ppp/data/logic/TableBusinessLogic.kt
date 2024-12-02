package com.dirtfy.ppp.data.logic

import android.util.Log
import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.api.RecordApi
import com.dirtfy.ppp.data.api.TableApi
import com.dirtfy.ppp.data.api.TransactionManager
import com.dirtfy.ppp.data.dto.feature.record.DataRecord
import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.ppp.data.dto.feature.record.DataRecordType
import com.dirtfy.ppp.data.dto.feature.table.DataTable
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup
import com.dirtfy.ppp.data.dto.feature.table.DataTableOrder
import com.dirtfy.ppp.data.logic.common.BusinessLogic
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class TableBusinessLogic @Inject constructor(
    private val tableApi: TableApi,
    private val recordApi: RecordApi,
    private val transactionManager: TransactionManager<Transaction> // TODO com.google.firebase.firestore.Transaction 숨기기
): BusinessLogic {

    private fun isInValidTableNumber(tableNumber: Int): Boolean {
        return tableNumber !in 1..11
    }

    fun readTables() = operate {
        val tableList = tableApi.readAllTable()
        tableList
    }

    fun readOrders(tableNumber: Int) = operate {
        if (isInValidTableNumber(tableNumber))
            throw TableException.InvalidTableNumber()

        val group = tableApi.readTable(tableNumber).group

        val tableOrderList = tableApi.readAllOrder(group)
        tableOrderList
    }

    fun tableStream() = tableApi.tableStream()
    fun orderStream(tableNumber: Int) = tableApi.orderStream(tableNumber)

    fun mergeTables(tableList: List<DataTable>) = operate {
        if (tableList.size <= 1)
            throw TableException.NonEnoughMergingTargets()

        for (table in tableList) {
            if (isInValidTableNumber(table.number))
                throw TableException.InvalidTableNumber()
        }

        val groupUniqueList = tableList.map {
            it.group
        }.toSet().toList()

        val groupOrderMap = mutableMapOf<Int, List<DataTableOrder>>()
        val orderCombineList = mutableListOf<DataTableOrder>()

        groupUniqueList.map {
            val orderList = tableApi.readAllOrder(it)
            groupOrderMap[it] = orderList
            orderList
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

        transactionManager.transaction { transaction ->
            groupUniqueList.forEach {
                tableApi.deleteOrders(it, groupOrderMap[it] ?: emptyList(), transaction)
            }

            val baseGroupNumber = groupUniqueList[0]
            var baseGroup = DataTableGroup(
                group = baseGroupNumber,
                member = tableList.filter { it.number == baseGroupNumber }.map { it.number }
            )

            orderCombineList.forEach {
                tableApi.setOrder(baseGroupNumber, it, transaction)
            }

            groupUniqueList.filter {
                it != baseGroupNumber
            }.forEach { targetGroupNumber ->
                val targetGroup = DataTableGroup(
                    group = targetGroupNumber,
                    member = tableList.filter { it.number == targetGroupNumber }.map { it.number }
                )
                baseGroup = tableApi.combineGroup(baseGroup, targetGroup, transaction)
            }

            baseGroupNumber
        }
    }

    private suspend fun cleanGroup(group: Int) {
        tableApi.apply {
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
        val group = tableApi.readTable(tableNumber).group

        val orderList = tableApi.readAllOrder(group)

        val payment = recordApi.create(
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
        val group = tableApi.readTable(tableNumber).group

        val orderList = tableApi.readAllOrder(group)

        val payment = recordApi.create(
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
        val group = tableApi.readTable(tableNumber).group

        val orderList = tableApi.readAllOrder(group)

        val nowBalance = recordApi.readSumOf("type", "$accountNumber", "income")
        val totalPrice = orderList.calcTotalPrice()

        if (nowBalance < totalPrice)
            throw TableException.InvalidPay()

        val payment = recordApi.create(
            record = DataRecord(
                income = -totalPrice,
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
        tableApi.run {
            val group = readTable(tableNumber).group

            val count: Int
            if (isOrderExist(group, menuName)) {
                val order = readOrder(group, menuName)
                count = order.count + 1
                setOrder(
                    group,
                    order.copy(count = count)
                )
            } else {
                count = 1
                setOrder(
                    group,
                    DataTableOrder(
                        name = menuName,
                        price = menuPrice,
                        count = count
                    )
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
        tableApi.run {
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
                        setOrder(
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
        val group = tableApi.readTable(tableNumber).group
        group
    }
}