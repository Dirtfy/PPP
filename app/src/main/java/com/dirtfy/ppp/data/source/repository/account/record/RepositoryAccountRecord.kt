package com.dirtfy.ppp.data.source.repository.account.record

data class RepositoryAccountRecord(
    val issuedName: String?,
    val difference: Int?,
    val result: Int?,
    val timestamp: Long?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )
}
