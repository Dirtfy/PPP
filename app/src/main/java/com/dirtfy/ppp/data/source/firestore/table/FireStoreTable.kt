package com.dirtfy.ppp.data.source.firestore.table

import com.dirtfy.ppp.common.exception.TableException
import com.dirtfy.ppp.data.dto.DataTable

data class FireStoreTable(
    val number: Int?,
    val group: Int?
) {
    constructor(): this(
        null,
        null
    )

    companion object {
        fun DataTable.convertToFireStoreTable(): FireStoreTable{
            return FireStoreTable(
                number = number,
                group = group
            )
        }
    }

    fun convertToDataTable(): DataTable {
        return DataTable(
            number = number?: throw TableException.NumberLoss(),
            group = group?: throw TableException.GroupLoss(),
        )
    }
}
