package com.dirtfy.ppp.model.selling.menu.managing

data class _MenuData(
    val name: String?,
    val price: Int?
){
    constructor() : this(null, null)
}
