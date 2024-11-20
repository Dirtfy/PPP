package com.dirtfy.ppp.common.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dirtfy.ppp.ui.controller.feature.account.AccountController
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountViewModel
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel.MenuViewModel
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordViewModel
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class AndroidViewModelModule {

    @Provides
    fun providesMenuController(
        @ActivityContext context: Context
    ): MenuController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[MenuViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountController(
        @ActivityContext context: Context
    ): AccountController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesRecordController(
        @ActivityContext context: Context
    ): RecordController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[RecordViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesTableController(
        @ActivityContext context: Context
    ): TableController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[TableViewModel::class.java]
        return viewModel
    }
}