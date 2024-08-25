package com.dirtfy.ppp.data.source.firestore.table

data class FireStoreTableOrder(
    val name: String?,
    val price: Int?,
    val count: Int?
) {
    constructor(): this(
        null,
        null,
        null
    )
}
