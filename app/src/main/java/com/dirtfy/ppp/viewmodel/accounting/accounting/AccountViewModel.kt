package com.dirtfy.ppp.viewmodel.accounting.accounting

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract.DTO.Account
import com.dirtfy.ppp.model.accounting.accounting.AccountRepository
import com.dirtfy.ppp.viewmodel.SingleViewModel

class AccountViewModel: SingleViewModel<Account>() {

    override val repository: Repository<Account>
        get() = AccountRepository
    override val initialValue: Account
        get() = Account()

}