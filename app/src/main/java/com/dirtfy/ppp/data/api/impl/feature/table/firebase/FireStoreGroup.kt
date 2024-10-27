package com.dirtfy.ppp.data.api.impl.feature.table.firebase

data class FireStoreGroup(
    val member: List<Int>?
) {
    constructor(): this(null)
}
