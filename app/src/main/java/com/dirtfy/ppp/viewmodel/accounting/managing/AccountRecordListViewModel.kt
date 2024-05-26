package com.dirtfy.ppp.viewmodel.accounting.managing

import com.dirtfy.ppp.model.Repository
import com.dirtfy.ppp.model.accounting.managing.AccountRecordData
import com.dirtfy.ppp.model.accounting.managing.AccountRecordRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class AccountRecordListViewModel: ListViewModel<AccountRecordData>() {

    override val repository: Repository<AccountRecordData>
        get() = AccountRecordRepository
}