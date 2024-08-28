package com.dirtfy.ppp.di

import com.dirtfy.ppp.ui.presenter.controller.MenuController
import com.dirtfy.ppp.ui.presenter.viewmodel.MenuViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UiModule {

    @Binds
    abstract fun bindMenuController(
        menuViewModel: MenuViewModel
    ): MenuController
}