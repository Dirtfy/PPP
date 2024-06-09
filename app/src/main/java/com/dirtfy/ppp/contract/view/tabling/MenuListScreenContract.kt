package com.dirtfy.ppp.contract.view.tabling

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.User
import com.dirtfy.ppp.contract.viewmodel.TablingContract

object MenuListScreenContract {

    interface API {

        @Composable
        fun MenuList(
            menuList: List<TablingContract.DTO.Menu>,
            user: User,
            modifier: Modifier
        )

        @Composable
        fun MenuItem(
            menu: TablingContract.DTO.Menu,
            user: User,
            modifier: Modifier
        )

    }

}