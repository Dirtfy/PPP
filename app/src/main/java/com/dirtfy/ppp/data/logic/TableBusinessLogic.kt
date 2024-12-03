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

    fun readOrders(groupNumber: Int) = operate {
        if (!tableApi.isGroupExist(groupNumber))
            throw TableException.GroupLoss()

        val tableOrderList = tableApi.readAllOrder(groupNumber)
        tableOrderList
    }

    fun tableStream() = tableApi.tableStream()
    fun orderStream(tableNumber: Int) = tableApi.orderStream(tableNumber)

    fun mergeTables(tableList: List<DataTable>) = operate {
        if (tableList.isEmpty())
            throw TableException.NonEnoughMergingTargets()

        for (table in tableList) {
            if (isInValidTableNumber(table.number))
                throw TableException.InvalidTableNumber()
        }

        val groupUniqueList = tableList.map {
            it.group
        }.filter {
            it != DataTable.GROUP_NOT_ASSIGNED
        }.toSet().toList()

        val orderCombineList = mutableListOf<DataTableOrder>()

        groupUniqueList.map {
            val orderList = tableApi.readAllOrder(it)
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
            val mergedGroup = DataTableGroup(
                member = tableList.map { it.number }
            )

            val createdGroup = tableApi.createGroup(mergedGroup, transaction)
            Log.d("WeGlonD", "merge - group created")
            orderCombineList.forEach {
                tableApi.setOrder(createdGroup.group, it, transaction)
            }
            Log.d("WeGlonD", "merge - order list set")
            groupUniqueList.forEach {
                tableApi.deleteGroup(it, transaction)
            }
            Log.d("WeGlonD", "merge - original group deleted")
            createdGroup.group
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
        groupNumber: Int
    ) = operate {
        val orderList = tableApi.readAllOrder(groupNumber)

        val payment = recordApi.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Cash.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(groupNumber)
        payment
    }
    fun payTableWithCard(
        groupNumber: Int
    ) = operate {
        val orderList = tableApi.readAllOrder(groupNumber)

        val payment = recordApi.create(
            record = DataRecord(
                income = orderList.calcTotalPrice(),
                type = DataRecordType.Card.name
            ),
            detailList = orderList.map{ it.convertToRecordDetail() }
        )
        // TODO 다른 DB간 transaction 처리

        cleanGroup(groupNumber)
        payment
    }
    fun payTableWithPoint(
        groupNumber: Int,
        accountNumber: Int,
        issuedName: String
    ) = operate {
        val orderList = tableApi.readAllOrder(groupNumber)

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

        cleanGroup(groupNumber)
        payment
    }

    fun addOrder(
        groupNumber: Int,
        menuName: String,
        menuPrice: Int,
        nowCount: UInt
    ) = operate {
        Log.d("WeGlonD", "service addOrder")
        transactionManager.transaction { transaction ->
            tableApi.run {
                if (nowCount.toInt() == 0) {
                    setOrder(
                        groupNumber,
                        DataTableOrder(
                            name = menuName,
                            price = menuPrice,
                            count = 1
                        ),
                        transaction
                    )
                } else {
                    incrementOrder(
                        groupNumber,
                        menuName,
                        transaction
                    )
                }

                DataTableOrder(
                    menuName,
                    menuPrice,
                    nowCount.toInt() + 1
                )
            }
        }
    }

    fun cancelOrder(
        groupNumber: Int,
        menuName: String,
        menuPrice: Int,
        nowCount: UInt
    ) = operate {
        transactionManager.transaction { transaction ->
            tableApi.run {
                when (nowCount.toInt()) {
                    0 -> throw TableException.NonEnoughMenuToCancel()
                    1 -> deleteOrder(groupNumber, menuName, transaction)
                    else -> {
                        decrementOrder(
                            groupNumber,
                            menuName,
                            transaction
                        )
                    }
                }

                DataTableOrder(
                    menuName,
                    menuPrice,
                    nowCount.toInt() - 1
                )
            }
        }
    }

}