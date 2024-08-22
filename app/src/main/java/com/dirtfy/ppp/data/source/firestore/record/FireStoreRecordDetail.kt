package com.dirtfy.ppp.data.source.firestore.record

data class FireStoreRecordDetail(
    val name: String?,
    val amount: Int?,
    val count: Int?
) {
    constructor() : this(null, null, null)
}
