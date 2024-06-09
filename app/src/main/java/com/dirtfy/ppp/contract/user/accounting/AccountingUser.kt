package com.dirtfy.ppp.contract.user.accounting

import com.dirtfy.ppp.contract.user.accounting.barcoding.BarcodingUser

interface AccountingUser: BarcodingUser {
    fun checkSearchClue() {}
    fun checkAccountList() {}

    fun searchByTyping() {}
    fun searchByBarcode() {}

    fun goToManaging() {}
}