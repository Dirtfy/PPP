package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.TablingContract

object TableScreenContract {

    interface API {

        @Composable
        fun Table(
            table: TablingContract.DTO.Table,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun TableLayout(
            tableList: List<TablingContract.DTO.Table>,
            user: User,
            modifier: Modifier
        )

    }
}