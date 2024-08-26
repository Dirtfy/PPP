package com.dirtfy.ppp.data.source.firestore.table

data class FireStoreGroup(
    val member: List<Int>?
) {
    constructor(): this(null)
}
