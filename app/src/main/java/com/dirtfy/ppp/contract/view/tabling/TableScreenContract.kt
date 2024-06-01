package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.User

object TableScreenContract {

    object DTO {

        data class Table(
            val number: String,
            val color: ULong
        )

    }

    interface API {

        @Composable
        fun Table(
            table: DTO.Table,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun TableLayout(
            tableList: List<DTO.Table>,
            user: User,
            modifier: Modifier
        )

    }
}