package com.dirtfy.ppp.common.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dirtfy.ppp.ui.controller.feature.menu.MenuController
import com.dirtfy.ppp.ui.controller.feature.account.AccountController
import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.controller.feature.account.AccountUpdateController
import com.dirtfy.ppp.ui.controller.feature.record.RecordController
import com.dirtfy.ppp.ui.controller.feature.record.RecordDetailController
import com.dirtfy.ppp.ui.controller.feature.table.TableController
import com.dirtfy.ppp.ui.controller.feature.menu.impl.viewmodel.MenuViewModel
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountCreateViewModel
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountDetailViewModel
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountUpdateViewModel
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountViewModel
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordDetailViewModel
import com.dirtfy.ppp.ui.controller.feature.record.impl.viewmodel.RecordViewModel
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class UiModule {

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
    fun providesAccountCreateController(
        @ActivityContext context: Context
    ): AccountCreateController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountCreateViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountDetailController(
        @ActivityContext context: Context
    ): AccountDetailController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountDetailViewModel::class.java]
        return viewModel
    }

    @Provides
    fun providesAccountUpdateController(
        @ActivityContext context: Context
    ): AccountUpdateController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountUpdateViewModel::class.java]
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
    fun providesRecordDetailController(
        @ActivityContext context: Context
    ): RecordDetailController {
        val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[RecordDetailViewModel::class.java]
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