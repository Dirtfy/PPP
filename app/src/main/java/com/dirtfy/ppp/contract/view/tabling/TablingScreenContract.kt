package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.contract.viewmodel.user.User

object TablingScreenContract {

    interface API {

        @Composable
        fun Main(
            viewModel: TablingContract.API,
            modifier: Modifier,
            user: User
        )

        @Composable
        fun InstantMenuCreation(
            menu: TablingContract.DTO.Menu,
            user: User,
            modifier: Modifier
        )

    }
}