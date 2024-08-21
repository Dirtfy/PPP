package com.dirtfy.ppp.data.source.repository.menu

data class RepositoryMenu(
    val name: String?,
    val price: Int?
) {
    constructor() : this(null, null)
}
