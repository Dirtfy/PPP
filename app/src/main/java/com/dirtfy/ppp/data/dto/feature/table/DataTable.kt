package com.dirtfy.ppp.data.dto.feature.table

data class DataTable(
    val number: Int,
    val group: Int = GROUP_NOT_ASSIGNED
) {
    companion object {
        const val GROUP_NOT_ASSIGNED = -1
    }
}
