package com.dirtfy.ppp.data.api.impl.feature.table.firebase

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.feature.table.DataTableGroup

data class FireStoreGroup(
    val member: List<Int>?
) {
    constructor(): this(null)

    companion object {
        fun DataTableGroup.convertToFireStoreGroup(): FireStoreGroup {
            return FireStoreGroup(member = member)
        }
    }

    fun convertToDataTableGroup(groupNumber: Int): DataTableGroup {
        return DataTableGroup(
            group = groupNumber,
            member = member ?: throw TableException.MemberLoss()
        )
    }
}
