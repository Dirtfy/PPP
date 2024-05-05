package com.dirtfy.ppp.ordering.menuManaging.model

data class _MenuData(
    val name: String?,
    val price: Int?
){
    constructor() : this(null, null)
}
