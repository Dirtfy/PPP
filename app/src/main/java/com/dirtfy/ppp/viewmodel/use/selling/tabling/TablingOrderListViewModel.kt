package com.dirtfy.ppp.viewmodel.use.selling.tabling

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract
import com.dirtfy.ppp.contract.model.selling.TableModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract.DTO.Order
import com.dirtfy.ppp.contract.viewmodel.selling.tabling.TablingViewModelContract.DTO.Total
import com.dirtfy.ppp.model.accounting.accounting.AccountRepository
import com.dirtfy.ppp.model.selling.recording.SalesRecordRepository
import com.dirtfy.ppp.model.selling.tabling.TableManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TablingOrderListViewModel: TablingViewModelContract.OrderList.API, Tagger {

    private val tableModel: TableModelContract.API = TableManager
    private val salesModel: SalesRecordModelContract.API = SalesRecordRepository
    private val accountModel: AccountModelContract.API = AccountRepository

    private val _orderList: MutableState<List<Order>>
    = mutableStateOf(listOf())
    override val orderList: State<List<Order>>
        get() = _orderList

    private val _total: MutableState<Total>
    = mutableStateOf(Total("0"))
    override val total: State<Total>
        get() {
            var sum = 0
            for (order in _orderList.value) {
                sum += order.price.toInt() * order.count.toInt()
            }
            return mutableStateOf(Total(sum.toString()))
        }

    override fun checkOrder(tableNumber: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val table = tableModel.checkTable(tableNumber)

            val orderList = mutableListOf<Order>()
            for(i in table.menuNameList.indices) {
                val menuName = table.menuNameList[i]
                val menuPrice = table.menuPriceList[i]

                val index = orderList.find { it.name == menuName }
                if (index == null) {
                    orderList.add(Order(
                        name = menuName,
                        price = menuPrice.toString(),
                        count = "1"
                    ))
                }
                else {
                    orderList.replaceAll {
                        return@replaceAll if (it.name == menuName) {
                            it.copy(count = (it.count.toInt()+1).toString())
                        } else {
                            it
                        }
                    }
                }
            }

            _orderList.value = orderList

            Log.d(TAG, "check order end")
        }
    }

    override fun cancelMenu(menu: TablingViewModelContract.DTO.Menu) {
        val nowList = _orderList.value.toMutableList()

        val index = nowList.indexOfFirst { it.name == menu.name }
        if (index == -1) {
            Log.e(TAG, "menu is not in order list")
            return
        }
        else {
            val nowOrder = nowList[index]

            if(nowOrder.count.toInt() == 1) {
                nowList.removeAt(index)
            }
            else {
                nowList.replaceAll {
                    return@replaceAll if (it.name == menu.name) {
                        it.copy(count = (it.count.toInt()-1).toString())
                    } else {
                        it
                    }
                }
            }
        }

        _orderList.value = nowList
    }

    override fun payTable(
        tableNumber: Int,
        payment: TablingViewModelContract.DTO.Payment,
        pointAccountNumber: String?
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val priceMap = mutableMapOf<String, Int>()
            val countMap = mutableMapOf<String, Int>()

            _orderList.value.forEach {
                priceMap[it.name] = it.price.toInt()
                countMap[it.name] = it.count.toInt()
            }

            salesModel.create(
                SalesRecordModelContract.DTO.Sales(
                    salesID = null,
                    pointAccountNumber = when(payment) {
                        TablingViewModelContract.DTO.Payment.Point -> pointAccountNumber
                        else -> payment.name
                    },
                    menuPriceMap = priceMap,
                    menuCountMap = countMap
            ))

            accountModel.update {
                if (it.accountNumber == pointAccountNumber) {
                    it.copy(balance = it.balance - total.value.price.toInt())
                } else {
                    it
                }
            }
        }
    }
}