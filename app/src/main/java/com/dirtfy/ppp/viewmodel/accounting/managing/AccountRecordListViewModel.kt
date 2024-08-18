package com.dirtfy.ppp.viewmodel.accounting.managing

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.contract.model.accounting.AccountRecordModelContract.DTO.AccountRecord
import com.dirtfy.ppp.model.accounting.managing.AccountRecordRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class AccountRecordListViewModel: ListViewModel<AccountRecord>() {

    override val repository: Repository<AccountRecord>
        get() = AccountRecordRepository
}