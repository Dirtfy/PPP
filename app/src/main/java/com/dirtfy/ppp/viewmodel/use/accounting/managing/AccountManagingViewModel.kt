package com.dirtfy.ppp.viewmodel.use.accounting.managing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.contract.model.accounting.AccountRecordModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract.DTO.Account
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract.DTO.Record
import com.dirtfy.ppp.model.accounting.managing.AccountRecordRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.Calendar

class AccountManagingViewModel: ViewModel(), AccountManagingViewModelContract.API {

    private val accountRecordModel: AccountRecordModelContract.API = AccountRecordRepository

    private val _accountDetail: MutableState<Account>
    = mutableStateOf(
        Account("","","","","")
    )
    override val accountDetail: State<Account>
        get() = _accountDetail

    override fun setAccountDetail(account: Account) {
        _accountDetail.value = account
    }

    private val _rawRecordList: MutableList<AccountRecordModelContract.DTO.AccountRecord>
    = mutableListOf()
    private val _recordList: MutableState<List<Record>>
    = mutableStateOf(listOf())
    override val recordList: State<List<Record>>
        get() = _recordList

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
    private fun AccountRecordModelContract.DTO.AccountRecord.convertToRecord(): Record {
        return Record(
            timestamp = timestampFormatting(this.timestamp),
            amount = this.amount.toString(),
            result = this.result.toString(),
            userName = this.userName
        )
    }
    private fun Record.convertToModelRecord(): AccountRecordModelContract.DTO.AccountRecord {
        return AccountRecordModelContract.DTO.AccountRecord(
            amount = this.amount.toInt(),
            result = this.result.toInt(),
            userName = this.userName
        )
    }

    override fun checkAccountRecordList() {
        viewModelScope.launch {
            _rawRecordList.clear()

            _rawRecordList.addAll(accountRecordModel.read { true })

            _recordList.value = _rawRecordList.map { it.convertToRecord() }
        }
    }

    override fun addRecord() {
        viewModelScope.launch {
            accountRecordModel.create(_newRecord.value.convertToModelRecord())

            _rawRecordList.add(_newRecord.value.convertToModelRecord())
            _recordList.value = _rawRecordList.map { it.convertToRecord() }
        }
    }

    private val _newRecord: MutableState<Record>
    = mutableStateOf(Record("","","",""))
    override val newRecord: State<Record>
        get() = _newRecord

    override fun setNewRecord(now: Record) {
        _newRecord.value = now
    }
}