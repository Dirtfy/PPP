package com.dirtfy.ppp.contract.user.selling.tabling

interface TablingUser {
    fun checkOrder() {}
    fun checkTable() {}
    fun checkMenu() {}

    fun mergeTable() {}
    fun cleanTable() {}
    fun payTable() {}

    fun orderMenu() {}
    fun orderInstantMenu() {}
    fun cancelMenu() {}
}