package com.dirtfy.ppp.contract.viewmodel.user.selling.tabling

interface TablingUser {
    fun checkOrder()
    fun checkTable()
    fun checkMenu()

    fun selectTable()
    fun deselectTable()
    fun mergeTable()
    fun cleanTable()
    fun payTable()

    fun orderMenu()
    fun orderInstantMenu()
    fun cancelMenu()
}