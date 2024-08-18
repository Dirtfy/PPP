package com.dirtfy.ppp.common

import androidx.compose.runtime.State
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract

object DummyAccountingViewModel: AccountingViewModelContract.API {
    override val searchClue: State<String>
        get() = TODO("Not yet implemented")

    override fun clueChanged(now: String) {
        TODO("Not yet implemented")
    }

    override fun searchByClue() {
        TODO("Not yet implemented")
    }

    override val accountList: State<List<AccountingViewModelContract.DTO.Account>>
        get() = TODO("Not yet implemented")

    override fun checkAccountList() {
        TODO("Not yet implemented")
    }

    override fun buildAccountArgumentString(data: AccountingViewModelContract.DTO.Account): String {
        TODO("Not yet implemented")
    }
}