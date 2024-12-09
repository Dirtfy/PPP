package com.dirtfy.ppp.data.api

interface ApiProvider {
    val accountApi: AccountApi
    val recordApi: RecordApi
    val menuApi: MenuApi
    val tableApi: TableApi
}