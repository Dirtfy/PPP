package com.dirtfy.ppp.common.di

import com.dirtfy.ppp.ui.controller.feature.account.AccountCreateController
import com.dirtfy.ppp.ui.controller.feature.account.AccountDetailController
import com.dirtfy.ppp.ui.controller.feature.account.AccountListController
import com.dirtfy.ppp.ui.controller.feature.account.AccountUpdateController
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountCreateControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountDetailControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountListControllerImpl
import com.dirtfy.ppp.ui.controller.feature.account.impl.viewmodel.AccountUpdateControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.TableMenuController
import com.dirtfy.ppp.ui.controller.feature.table.TableMergeController
import com.dirtfy.ppp.ui.controller.feature.table.TableOrderController
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableMenuControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableMergeControllerImpl
import com.dirtfy.ppp.ui.controller.feature.table.impl.viewmodel.TableOrderControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {
    @Binds
    abstract fun bindsTableMergeController(
        controllerImpl: TableMergeControllerImpl
    ): TableMergeController

    @Binds
    abstract fun bindsTableOrderController(
        controllerImpl: TableOrderControllerImpl
    ): TableOrderController

    @Binds
    abstract fun bindsTableMenuController(
        controllerImpl: TableMenuControllerImpl
    ): TableMenuController

    @Binds
    abstract fun bindsAccountListController(
        controllerImpl: AccountListControllerImpl
    ): AccountListController

    @Binds
    abstract fun bindsAccountCreateController(
        controllerImpl: AccountCreateControllerImpl
    ): AccountCreateController

    @Binds
    abstract fun bindsAccountDetailController(
        controllerImpl: AccountDetailControllerImpl
    ): AccountDetailController

    @Binds
    abstract fun bindsAccountUpdateController(
        controllerImpl: AccountUpdateControllerImpl
    ): AccountUpdateController
}