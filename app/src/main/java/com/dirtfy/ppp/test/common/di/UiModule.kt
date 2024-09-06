package com.dirtfy.ppp.test.common.di

import com.dirtfy.ppp.test.ui.presenter.contract.account.AccountController
import com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.account.AccountViewModel
import com.dirtfy.ppp.ui.presenter.controller.MenuController
import com.dirtfy.ppp.ui.presenter.viewmodel.MenuViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UiModule {

    @Binds
    abstract fun bindMenuController(
        menuViewModel: MenuViewModel
    ): MenuController

    @Binds
    abstract fun bindAccountController(
        accountViewModel: AccountViewModel
    ): AccountController

}