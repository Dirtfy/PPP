package com.dirtfy.ppp.data.api

import com.google.firebase.firestore.Transaction

interface ApiProvider {
    val accountApi: AccountApi
    val recordApi: RecordApi<Transaction>
    val menuApi: MenuApi
    val tableApi: TableApi<Transaction>
}