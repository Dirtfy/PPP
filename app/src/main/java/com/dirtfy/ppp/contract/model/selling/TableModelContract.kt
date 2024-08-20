package com.dirtfy.ppp.contract.model.selling

object TableModelContract {
    object DTO {
        data class Table(
            val tableNumber: Int,
            val menuNameList: List<String>,
            val menuPriceList: List<Int>
        )
    }

    interface API {
        suspend fun isSetup(tableNumber: Int): Boolean
        suspend fun isSetup(tableNumberList: List<Int>): List<Boolean>

        suspend fun setupTable(tableNumber: Int)
        suspend fun checkTable(tableNumber: Int): DTO.Table
        suspend fun checkTables(tableNumberList: List<Int>): List<DTO.Table>

        suspend fun updateMenu(tableData: DTO.Table)
        suspend fun mergeTable(baseTable: DTO.Table, mergingTable: DTO.Table)

        suspend fun cleanTable(tableNumber: Int)
    }
}