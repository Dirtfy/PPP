package com.dirtfy.ppp.data.dto.feature.table

data class DataTableGroup(
    val group: Int = DataTable.GROUP_NOT_ASSIGNED,
    val member: List<Int> = emptyList()
)
