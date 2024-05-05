package com.dirtfy.ppp.accounting.accounting.viewmodel

import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.model.AccountRepository
import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.viewmodel.SingleViewModel

class AccountViewModel: SingleViewModel<AccountData>() {

    override val repository: Repository<AccountData>
        get() = AccountRepository
    override val initialValue: AccountData
        get() = AccountData()
}