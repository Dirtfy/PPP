package com.dirtfy.ppp.viewmodel.accounting.accounting

import com.dirtfy.ppp.model.Repository
import com.dirtfy.ppp.model.accounting.accounting.AccountData
import com.dirtfy.ppp.model.accounting.accounting.AccountRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class AccountListViewModel : ListViewModel<AccountData>() {

    override val repository: Repository<AccountData>
        get() = AccountRepository

}