package com.dirtfy.ppp.data.logic

import android.util.Log
import androidx.collection.mutableIntSetOf
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
    private val tableApi: TableApi<Transaction>,
    private val recordApi: RecordApi<Transaction>,
    private val transactionManager: TransactionManager<Transaction> // TODO com.google.firebase.firestore.Transaction 숨기기
): BusinessLogic {

    private fun isInValidTableNumber(tableNumber: Int): Boolean {
        return tableNumber !in 1..11
    }

    fun readTables() = operate {
        val tableList = tableApi.readAllGroupedTable()
        val groupedTableSet = mutableIntSetOf()
        tableList.forEach { groupedTableSet.add(it.number) }
        val ungroupedTableList = (1..11).filter { !groupedTableSet.contains(it) }
            .map { DataTable(
                number = it,
                group = DataTable.GROUP_NOT_ASSIGNED
            ) }
        tableList + ungroupedTableList
    }

    fun readOrders(groupNumber: Int) = operate {
        if (!tableApi.isGroupExist(groupNumber))
            throw TableException.GroupLoss()

        val tableOrderList = tableApi.readAllOrder(groupNumber)
        tableOrderList
    }

    fun tableStream() = tableApi.tableStream()
    fun orderStream(tableNumber: Int) = tableApi.orderStream(tableNumber)

    fun getTableGroupLock() = operate {
        transactionManager.transaction { transaction ->
            tableApi.checkTableGroupLock(transaction)
            tableApi.getTableGroupLock(transaction)
            true
        }
    }

    fun releaseTableGroupLock() = operate {
        transactionManager.transaction {transaction ->
            tableApi.releaseTableGroupLock(transaction)
            true
        }
    }

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

        val orderListMap = mutableMapOf<Int, List<DataTableOrder>>()
        groupUniqueList.map {
            val orderList = tableApi.readAllOrder(it)
            orderListMap[it] = orderList
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
                tableApi.deleteGroup(it, orderListMap[it]!!, transaction)
            }
            Log.d("WeGlonD", "merge - original group deleted")
            createdGroup.group
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
        groupNumber: Int,
        orderList: List<DataTableOrder>
    ) = operate {
        transactionManager.transaction { transaction ->
            tableApi.checkTableGroupLock(transaction)
            val payment = payTable(
                orderList = orderList,
                type = DataRecordType.Cash.name,
                transaction = transaction
            )

            tableApi.getTableGroupLock(transaction)
            cleanGroup(groupNumber, orderList, transaction)
            tableApi.releaseTableGroupLock(transaction)
            payment
        }
    }

    fun payTableWithCard(
        groupNumber: Int,
        orderList: List<DataTableOrder>
    ) = operate {
        transactionManager.transaction { transaction ->
            tableApi.checkTableGroupLock(transaction)
            val payment = payTable(
                orderList = orderList,
                type = DataRecordType.Card.name,
                transaction = transaction
            )

            tableApi.getTableGroupLock(transaction)
            cleanGroup(groupNumber, orderList, transaction)
            tableApi.releaseTableGroupLock(transaction)
            payment
        }
    }

    fun payTableWithPoint(
        groupNumber: Int,
        accountNumberString: String,
        issuedName: String,
        orderList: List<DataTableOrder>
    ) = operate {
        if (accountNumberString == "")
            throw TableException.BlankAccountNumber()

        val nowBalance = recordApi.readSumOf("type", accountNumberString, "income")
        val totalPrice = orderList.calcTotalPrice()

        if (nowBalance < totalPrice)
            throw TableException.InvalidPay()

        transactionManager.transaction { transaction ->
            tableApi.checkTableGroupLock(transaction)
            val payment = payTable(
                orderList = orderList,
                type = accountNumberString,
                issuedName = issuedName,
                transaction = transaction
            )

            tableApi.getTableGroupLock(transaction)
            cleanGroup(groupNumber, orderList, transaction)
            tableApi.releaseTableGroupLock(transaction)
            payment
        }
    }

    private fun payTable(
        orderList: List<DataTableOrder>,
        type: String,
        issuedName: String? = null,
        transaction: Transaction
    ): DataRecord {
        val absoluteTotalPrice = orderList.calcTotalPrice()
        val isPoint = !(type == DataRecordType.Cash.name || type == DataRecordType.Card.name)
        var record = DataRecord(
            income = if (isPoint) -absoluteTotalPrice else absoluteTotalPrice,
            type = type
        )
        if (issuedName != null) record = record.copy(issuedBy = issuedName)

        return recordApi.create(
            record = record,
            detailList = orderList.map { it.convertToRecordDetail() },
            transaction = transaction
        )
    }

    private fun cleanGroup(group: Int, orderList: List<DataTableOrder>, transaction: Transaction) {
        tableApi.deleteGroup(group, orderList, transaction)
    }

    fun dissolveGroup(groupNumber: Int, orderList: List<DataTableOrder>) = operate {
        transactionManager.transaction { transaction ->
            tableApi.getTableGroupLock(transaction)
            cleanGroup(groupNumber, orderList, transaction)
            tableApi.releaseTableGroupLock(transaction)
            groupNumber
        }
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