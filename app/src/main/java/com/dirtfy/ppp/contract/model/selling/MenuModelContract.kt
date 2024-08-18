package com.dirtfy.ppp.contract.model.selling

import com.dirtfy.ppp.common.Repository

object MenuModelContract {
    object DTO {
        data class Menu(
            val menuID: String?,
            val name: String,
            val price: Int
        )
    }

    interface API: Repository<DTO.Menu>
}