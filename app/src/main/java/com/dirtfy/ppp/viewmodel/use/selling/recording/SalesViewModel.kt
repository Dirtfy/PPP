package com.dirtfy.ppp.viewmodel.use.selling.recording

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract.DTO.Menu
import com.dirtfy.ppp.contract.viewmodel.selling.sales.recording.SalesRecordingViewModelContract.DTO.Record
import com.dirtfy.ppp.model.selling.recording.SalesRecordRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.Calendar

class SalesViewModel: ViewModel(), SalesRecordingViewModelContract.API, Tagger {

    private val salesModel: SalesRecordModelContract.API = SalesRecordRepository

    private val _rawRecordList: MutableList<SalesRecordModelContract.DTO.Sales>
    = mutableListOf()
    private val _recordList: MutableState<List<Record>>
    = mutableStateOf(listOf())
    override val recordList: State<List<Record>>
        get() = _recordList

    private var _rawSelectedRecord: SalesRecordModelContract.DTO.Sales?
    = null
    private val _selectedRecord: MutableState<Record?>
    = mutableStateOf(null)
    override val selectedRecord: State<Record?>
        get() = _selectedRecord

    private fun timestampFormatting(timestamp: Timestamp): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp.seconds

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return "$year/$month/$day $hour:$minute"
    }
    private fun calcTotalPrice(
        countMap: Map<String, Int>,
        priceMap: Map<String, Int>
    ): Int {
        var total = 0

        for (key in countMap.keys) {
            total += countMap[key]!! * (priceMap[key]?: 0)
        }

        return total
    }
    private fun SalesRecordModelContract.DTO.Sales.convertToRecord(): Record {
        val timestamp = timestampFormatting(this.timestamp)
        val totalPrice = calcTotalPrice(this.menuCountMap, this.menuPriceMap)
        val payment = if(this.pointAccountNumber == null) {
            "cash"
        } else {
            "point"
        }

        return Record(
            timestamp = timestamp,
            totalPrice = totalPrice.toString(),
            payment = payment
        )
    }

    override fun checkSalesRecordList() {
        viewModelScope.launch {
            _rawRecordList.clear()

            _rawRecordList.addAll(salesModel.read { true })

            _recordList.value = _rawRecordList.map { it.convertToRecord() }
        }
    }

    override fun clickRecord(record: Record) {
        _rawSelectedRecord = _rawRecordList[_recordList.value.indexOf(record)]
    }

    private val _recordDetail: MutableState<List<Menu>>
    = mutableStateOf(listOf())
    override val recordDetail: State<List<Menu>>
        get() = _recordDetail

    override fun checkRecordDetail() {
        val detail = _rawRecordList.find {
            it.salesID == _rawSelectedRecord?.salesID
        }

        if (detail == null) {
            Log.e(TAG, "fail to find selected")
            return
        }

        val menuList = mutableListOf<Menu>()
        for(key in detail.menuCountMap.keys) {
            menuList.add(
                Menu(
                    name = key,
                    price = detail.menuPriceMap[key]!!.toString(),
                    count = detail.menuCountMap[key]!!.toString()
                )
            )
        }

        _recordDetail.value = menuList
    }
}