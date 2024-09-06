package com.dirtfy.ppp.test.common.di

import com.dirtfy.ppp.test.ui.dto.account.UiAccount
import com.dirtfy.ppp.test.ui.presenter.impl.viewmodel.account.AccountViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AccountModule {

    @Provides
    fun providesTargetAccount(
        viewModel: AccountViewModel
    ): UiAccount {
        return viewModel.selectedAccount
    }
}