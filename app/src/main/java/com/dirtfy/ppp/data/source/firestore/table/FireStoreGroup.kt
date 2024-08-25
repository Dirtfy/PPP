package com.dirtfy.ppp.data.source.firestore.table

data class FireStoreGroup(
    val group: Int?,
    val memberList: List<Int>?
) {
    constructor(): this(
        null,
        null
    )
}
