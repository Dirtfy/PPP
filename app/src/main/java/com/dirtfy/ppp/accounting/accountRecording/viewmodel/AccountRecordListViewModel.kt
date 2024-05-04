package com.dirtfy.ppp.accounting.accountRecording.viewmodel

import com.dirtfy.ppp.accounting.accountRecording.model.AccountRecordData
import com.dirtfy.ppp.accounting.accountRecording.model.AccountRecordRepository
import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.viewmodel.ListViewModel

class AccountRecordListViewModel: ListViewModel<AccountRecordData>() {

    override val repository: Repository<AccountRecordData>
        get() = AccountRecordRepository
}