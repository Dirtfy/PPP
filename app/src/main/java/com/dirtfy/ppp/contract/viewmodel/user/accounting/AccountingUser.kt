package com.dirtfy.ppp.contract.viewmodel.user.accounting

import com.dirtfy.ppp.contract.viewmodel.user.accounting.barcoding.BarcodingUser

interface AccountingUser: BarcodingUser {
    fun checkSearchClue()
    fun checkAccountList()

    fun searchByTyping()
    fun searchByBarcode()

    fun goToManaging()
}