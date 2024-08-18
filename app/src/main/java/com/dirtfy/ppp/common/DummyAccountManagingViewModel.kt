package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import com.dirtfy.ppp.contract.viewmodel.accounting.managing.AccountManagingViewModelContract

object DummyAccountManagingViewModel: AccountManagingViewModelContract.API {
    override val accountDetail: State<AccountManagingViewModelContract.DTO.Account>
        get() = TODO("Not yet implemented")

    override fun setAccountDetail(account: AccountManagingViewModelContract.DTO.Account) {
        TODO("Not yet implemented")
    }

    override val recordList: State<List<AccountManagingViewModelContract.DTO.Record>>
        get() = TODO("Not yet implemented")

    override fun checkAccountRecordList() {
        TODO("Not yet implemented")
    }

    override fun addRecord() {
        TODO("Not yet implemented")
    }

    override val newRecord: State<AccountManagingViewModelContract.DTO.Record>
        get() = TODO("Not yet implemented")

    override fun setNewRecord(now: AccountManagingViewModelContract.DTO.Record) {
        TODO("Not yet implemented")
    }
}