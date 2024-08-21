package com.dirtfy.ppp.data.source.repository.account

data class RepositoryAccount(
    val number: Int?,
    val name: String?,
    val phoneNumber: String?,
    val balance: Int?,
    val registerTimestamp: Long?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null
    )
}
